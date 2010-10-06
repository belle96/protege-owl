
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

/**
 * Interface representing OWL data value argument to atoms and built-ins
 */
public interface SWRLLiteralArgument extends BuiltInArgument, SWRLArgument
{
	DataValue getLiteral();
}