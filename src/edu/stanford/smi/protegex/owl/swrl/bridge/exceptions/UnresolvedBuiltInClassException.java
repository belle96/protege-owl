
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class UnresolvedBuiltInClassException extends BuiltInLibraryException 
{
  public UnresolvedBuiltInClassException(String ruleName, String prefix, String message) 
  { 
     super("unresolved built-in class for prefix '" + prefix + "' in rule '" + ruleName + "': " + message);
  } // UnresolvedBuiltInClassException
} // UnresolvedBuiltInClassException
