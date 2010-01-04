
package edu.stanford.smi.protegex.owl.swrl.bridge;

/**
 ** Interface representing OWL property arguments to atoms and built-ins
 */
public interface PropertyArgument extends BuiltInArgument, AtomArgument
{
  String getPropertyName();
  String getPrefixedPropertyName();
} // PropertyArgument
