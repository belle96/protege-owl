package edu.stanford.smi.protegex.owl.swrl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtom;

public abstract class DefaultSWRLAtom extends AbstractSWRLIndividual implements SWRLAtom {

  public DefaultSWRLAtom(KnowledgeBase kb, FrameID id) 
  {
    super(kb, id);
  } // DefaultSWRLAtom
    
  public DefaultSWRLAtom() {}
  
  // Should only be the getBrowserText of a child finds the atom to be in an invalid state.
  @Override
public String getBrowserText() 
  {
    return "<INVALID_ATOM>";
  } // getBrowserText

} // DefaultSWRLAtom


