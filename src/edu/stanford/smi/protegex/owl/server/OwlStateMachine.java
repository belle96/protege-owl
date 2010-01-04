package edu.stanford.smi.protegex.owl.server;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.framestore.FrameStore;
import edu.stanford.smi.protege.server.framestore.background.ServerCacheStateMachine;
import edu.stanford.smi.protege.server.framestore.background.ServerCachedState;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.impl.OWLSystemFrames;


public class OwlStateMachine implements ServerCacheStateMachine {
  private static transient Logger log = Log.getLogger(OwlStateMachine.class);
  
  private FrameStore fs;
  private final OWLModel kb;
  
  private Map<StateAndSlot, OwlState> transitionMap = new HashMap<StateAndSlot, OwlState>();
  
  

  public OwlStateMachine(FrameStore fs, OWLModel model) {
    this.fs = fs;
    this.kb = model;
    OWLSystemFrames systemFrames = model.getSystemFrames();
    synchronized (model) {
      addTransition(OwlState.Start, systemFrames.getDirectSuperclassesSlot(), OwlState.OwlExpr);
      
      addTransition(OwlState.Start, systemFrames.getOwlEquivalentClassProperty(), OwlState.OwlExpr);
      
      
      addTransition(OwlState.OwlExpr, systemFrames.getOwlIntersectionOfProperty(), OwlState.RDFList);
      addTransition(OwlState.OwlExpr, systemFrames.getDirectSuperclassesSlot(), OwlState.End);
      addTransition(OwlState.OwlExpr, systemFrames.getOwlSomeValuesFromProperty(), OwlState.End);
      
      addTransition(OwlState.RDFList, systemFrames.getRdfRestProperty(), OwlState.RDFList);
      addTransition(OwlState.RDFList, systemFrames.getRdfFirstProperty(), OwlState.OwlExpr);
      
      addTransition(OwlState.Start, systemFrames.getDirectInstancesSlot(), OwlState.UserIndividual);

    }
  }
  
  private void addTransition(OwlState start, Slot slot, OwlState end) {
    try {
      transitionMap.put(new StateAndSlot(start, slot), end);
    } catch (Exception e) {
      if (log.isLoggable(Level.FINE)) {
        log.fine("Exception caught creating transition " + 
            start + ", " + slot.getFrameID().getName() + " -> " + end + ": " + e);
        log.log(Level.FINER, "Exception = ", e);
      }
    }
  }
  
  public ServerCachedState getInitialState() {
      return OwlState.Start;
  }
  
  public OwlState nextState(ServerCachedState state, Frame beginningFrame, Slot slot, Frame endingFrame) {
    if (!(state instanceof OwlState)) {
        return null;
    }
    OwlState endState = transitionMap.get(new StateAndSlot((OwlState) state, slot));
    synchronized (kb) {
      if (endState != null && OwlState.allowTransition(fs, (OwlState) state, beginningFrame,  endState, endingFrame)) {
        return endState;
      }
    }
    return null;
  }

  private class StateAndSlot {
    private OwlState state;
    private Slot slot;
    
    public Slot getSlot() {
      return slot;
    }

    public OwlState getState() {
      return state;
    }

    public StateAndSlot(OwlState state, Slot slot) {
      this.state = state;
      this.slot = slot;
    }
    
    public int hashCode() {
      return state.ordinal() + slot.hashCode();
    }
    
    public boolean equals(Object o) {
      if (o == null || !(o instanceof StateAndSlot)) {
        return false;
      }
      StateAndSlot other = (StateAndSlot) o;
      return other.state.equals(state) && other.slot.equals(slot);
    }
    
  }
}
