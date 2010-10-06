
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.BuiltInArgumentImpl;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLVariable;

/**
 * Interface representing a variable argument to a SWRL atom or built-in
 */
public class SWRLVariableImpl extends BuiltInArgumentImpl implements SWRLVariable
{
  public SWRLVariableImpl(String variableName) { super(variableName); }
  
  public String toString() { return getVariableName(); }
  
  public int compareTo(BuiltInArgument o)
  {
  	return getVariableName().compareTo(o.getVariableName());
  }
} 
