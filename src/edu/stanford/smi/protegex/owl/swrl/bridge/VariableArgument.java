
package edu.stanford.smi.protegex.owl.swrl.bridge;

/**
 * Interface representing a variable argument to a SWRL atom or built-in
 */
public interface VariableArgument extends AtomArgument, BuiltInArgument
{
  String getVariableName();
}
