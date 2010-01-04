
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.VariableAtomArgument;

/**
 ** Interface representing an argument to a SWRL atom
 */
public class VariableAtomArgumentImpl implements VariableAtomArgument
{
  private String variableName;

  public VariableAtomArgumentImpl(String variableName) { this.variableName = variableName; }

  public String getVariableName() { return variableName; }

  public String toString() { return "?" + getVariableName(); }
} // VariableAtomArgumentImpl
