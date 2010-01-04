
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class InvalidBuiltInLibraryNameException extends SWRLRuleEngineBridgeException 
{
  public InvalidBuiltInLibraryNameException(String libraryNamespace) 
  { 
    super("Invalid built-in library name '" + libraryNamespace + "'"); 
  } // InvalidBuiltInLibraryNameException
} // InvalidBuiltInLibraryNameException
