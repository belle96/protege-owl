
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLArgument;

/**
 * Interface representing OWL named class arguments to atoms and built-ins
 */
public interface ClassArgument extends BuiltInArgument, SWRLArgument
{
  String getURI();
}
