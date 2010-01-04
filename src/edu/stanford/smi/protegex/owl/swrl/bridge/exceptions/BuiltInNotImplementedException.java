
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class BuiltInNotImplementedException extends BuiltInException 
{
  public BuiltInNotImplementedException() 
  { 
    super("built-in not yet implemented"); 
  } // BuiltInNotImplementedException

  public BuiltInNotImplementedException(String message) 
  { 
    super("built-in not yet implemented: " + message); 
  } // BuiltInNotImplementedException
} // BuiltInNotImplementedException

