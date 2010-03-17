
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.VariableArgument;

/**
 * Interface representing a variable argument to a SWRL atom or built-in
 */
public class VariableArgumentImpl extends BuiltInArgumentImpl implements VariableArgument
{
  public VariableArgumentImpl(String variableName) { super(variableName); }
  
  public String toString() { return getVariableName(); }
  
  public int compareTo(BuiltInArgument o)
  {
  	return getVariableName().compareTo(o.getVariableName());
  }
} 
