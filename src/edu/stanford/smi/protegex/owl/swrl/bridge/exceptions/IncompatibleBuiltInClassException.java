
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class IncompatibleBuiltInClassException extends BuiltInLibraryException 
{
  public IncompatibleBuiltInClassException(String ruleName, String prefix, String className, String message) 
  { 
    super("incompatible Java built-in class " + className + " defined for library prefix " + prefix + " (used in rule " + ruleName + "): " 
          + message);
  } //IncompatibleBuiltInClassException

} // IncompatibleBuiltInClassException
