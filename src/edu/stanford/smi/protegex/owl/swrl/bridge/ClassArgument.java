
package edu.stanford.smi.protegex.owl.swrl.bridge;

/**
 * Interface representing OWL named class arguments to atoms and built-ins
 */
public interface ClassArgument extends BuiltInArgument, AtomArgument
{
  String getURI();
} // ClassArgument
