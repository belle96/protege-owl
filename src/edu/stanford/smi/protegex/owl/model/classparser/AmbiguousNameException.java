package edu.stanford.smi.protegex.owl.model.classparser;

import edu.stanford.smi.protege.exception.ProtegeException;

public class AmbiguousNameException extends ProtegeException {
  
  public AmbiguousNameException(String msg) {
    super(msg);
  }

}
