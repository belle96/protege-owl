
package edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.VariableAtomArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.tmp.ArgumentImpl;

/**
 * Interface representing a variable argument to a SWRL atom
 */
public class VariableAtomArgumentImpl extends ArgumentImpl implements VariableAtomArgument
{
  public VariableAtomArgumentImpl(String variableName) { super(variableName); }
} // VariableAtomArgumentImpl
