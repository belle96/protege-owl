
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.VariableBuiltInArgument;

/**
 * Class representing a variable argument to a built-in
 */
public class VariableBuiltInArgumentImpl extends BuiltInArgumentImpl implements VariableBuiltInArgument
{
  public VariableBuiltInArgumentImpl(String variableName) { super(variableName); }

  public String toString() 
  {
    return "?" + getVariableName();
  } // toString
  
  public int compareTo(BuiltInArgument argument)
  {
  	return getVariableName().compareTo(argument.getVariableName());
  }
      
} // VariableBuiltInArgumentImpl
