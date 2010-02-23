
package edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.VariableBuiltInArgument;

/**
 * Interface representing a variable argument to a built-in
 */
public class VariableBuiltInArgumentImpl extends BuiltInArgumentImpl implements VariableBuiltInArgument
{
  public VariableBuiltInArgumentImpl(String variableName) { super(variableName); }

  public String toString() 
  {
    return "?" + getVariableName();
  } // toString      
      
} // VariableBuiltInArgumentImpl
