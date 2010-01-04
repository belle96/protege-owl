package edu.stanford.smi.protegex.owl.server;


import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.framestore.FrameStore;
import edu.stanford.smi.protege.server.framestore.background.ServerCachedState;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;



public enum OwlState implements ServerCachedState {
  Start, SubClass, OwlExpr, RDFList, UserIndividual, End;
  
  public static boolean allowTransition(FrameStore fs, OwlState startState, Frame startFrame, OwlState endState, Frame endingFrame) {
    switch (endState) {
    case OwlExpr:
      return isOWLAnonymous(fs, endingFrame);
    case SubClass:
      return !isOWLAnonymous(fs, endingFrame);
    case UserIndividual:
        return startFrame instanceof OWLNamedClass && !isOWLAnonymous(fs, startFrame) && !startFrame.isSystem();
    default:
      return true;
    }
  }
  
  private static boolean isOWLAnonymous(FrameStore fs, Frame f) {
      synchronized (f.getKnowledgeBase()) {
          return fs.getFrameName(f).startsWith("@");
      }
  }
}
