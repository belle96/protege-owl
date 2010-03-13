
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;

/**
 * Interface representing an argument to a built-in
 */
public interface BuiltInArgument extends Argument, Comparable<BuiltInArgument>
{
  void setBuiltInResult(BuiltInArgument builtInResult) throws BuiltInException;
  BuiltInArgument getBuiltInResult();
  MultiArgument getBuiltInMultiArgumentResult() throws BuiltInException;
  boolean hasBuiltInResult(); 
  boolean hasBuiltInMultiArgumentResult();
} 
