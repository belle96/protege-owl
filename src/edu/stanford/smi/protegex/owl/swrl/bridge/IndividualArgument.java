
package edu.stanford.smi.protegex.owl.swrl.bridge;

/**
 ** Interface representing OWL individual arguments to atoms and built-ins
 */
public interface IndividualArgument extends BuiltInArgument, AtomArgument
{
  String getURI();
  String getPrefixedIndividualName();
} // IndividualArgument
