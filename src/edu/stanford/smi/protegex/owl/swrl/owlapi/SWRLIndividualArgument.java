
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;

/**
 * Interface representing OWL individual arguments passed to atoms and built-ins
 */
public interface SWRLIndividualArgument extends BuiltInArgument, SWRLArgument
{
  String getURI();
}
