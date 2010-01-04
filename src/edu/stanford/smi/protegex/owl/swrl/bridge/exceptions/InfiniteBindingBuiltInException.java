
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class InfiniteBindingBuiltInException extends BuiltInException 
{
  public InfiniteBindingBuiltInException() 
  { 
    super("infinite bindings would be required to satisfy this built-in predicate");
  } // InfiniteBindingBuiltInException

  public InfiniteBindingBuiltInException(String builtInName, String message) 
  { 
    super("infinite bindings would be required to satisfy this built-in predicate: " + message); 
  } // InfiniteBindingBuiltInException
} // InfiniteBindingBuiltInException
