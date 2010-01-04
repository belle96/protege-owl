
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class IncompatibleBuiltInMethodException extends BuiltInException 
{
  public IncompatibleBuiltInMethodException(String ruleName, String prefix, String builtInMethodName, String message) 
  { 
    super("incompatible Java method defined for built-in'" + prefix + ":" + builtInMethodName + "' (used in rule '" + ruleName + "'): " 
          + message);
  } //IncompatibleBuiltInMethodException

} // IncompatibleBuiltInMethodException
