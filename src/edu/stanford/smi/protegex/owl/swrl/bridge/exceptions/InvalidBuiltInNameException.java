
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class InvalidBuiltInNameException extends BuiltInException 
{
  public InvalidBuiltInNameException(String ruleName, String builtInName) 
  { 
    super("unknown built-in '" + builtInName + "' in rule '" + ruleName + "'.");
  } // InvalidBuiltInNameException

  public InvalidBuiltInNameException(String builtInName) 
  { 
    super("unknown built-in '" + builtInName + "': ");
  } // InvalidBuiltInNameException
} // InvalidBuiltInNameException

