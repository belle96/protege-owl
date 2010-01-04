
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class InvalidBuiltInMethodsImplementationClass extends BuiltInException 
{
  public InvalidBuiltInMethodsImplementationClass(String className) 
  { 
    super("Class " + className + " does not implement the interface SWRLBuiltInMethods");
  } // InvalidBuiltInMethodsImplementationClass
} // InvalidBuiltInMethodsImplementationClass
