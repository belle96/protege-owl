
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLArgument;

/**
 * Interface representing OWL property arguments to atoms and built-ins
 */
public interface PropertyArgument extends BuiltInArgument, SWRLArgument
{
  String getURI();
}
