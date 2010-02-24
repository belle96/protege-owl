
package edu.stanford.smi.protegex.owl.swrl.bridge;

/**
 * Interface representing OWL individual arguments passed to atoms and built-ins
 */
public interface IndividualArgument extends BuiltInArgument, AtomArgument, Comparable<IndividualArgument>
{
  String getURI();
} // IndividualArgument
