
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class BuiltInMethodRuntimeException extends BuiltInException 
{
  public BuiltInMethodRuntimeException(String ruleName, String builtInName, String message, Throwable cause) 
  { 
    super("runtime exception in built-in " + builtInName + " in rule " + ruleName + ": " + message, cause); 
  } // BuiltInMethodRuntimeException
} // BuiltInMethodRuntimeException
