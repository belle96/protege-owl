
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class InvalidBuiltInArgumentNumberException extends BuiltInException 
{
  public InvalidBuiltInArgumentNumberException(int expecting, int actual) 
  { 
    super("invalid number of arguments - expecting " + expecting + " argument(s), got " + actual);
  } // InvalidBuiltInArgumentNumberException

  public InvalidBuiltInArgumentNumberException(int expecting, int actual, String message) 
  { 
    super("invalid number of arguments - expecting " + message + " " + expecting + " argument(s), got " + actual);
  } // InvalidBuiltInArgumentNumberException

} // InvalidBuiltInArgumentNumberException
