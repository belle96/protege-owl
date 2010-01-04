
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class InvalidBuiltInArgumentException extends BuiltInException
{
  public InvalidBuiltInArgumentException(int argumentNumber, String message) 
  { 
    super(message + " for (0-offset) argument #" + argumentNumber);
  } // InvalidBuiltInArgumentException

  public InvalidBuiltInArgumentException(String message) 
  { 
    super(message);
  } // InvalidBuiltInArgumentException

} // InvalidBuiltInArgumentException
