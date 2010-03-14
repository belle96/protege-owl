
package edu.stanford.smi.protegex.owl.swrl.bridge;

/**
 * Interface representing arguments to atoms and built-ins
 */
public interface Argument
{
	boolean isVariable();
  boolean isUnbound();
  boolean isBound();
  String getVariableName();
  void setVariableName(String variableName);
  void setUnbound();
}
