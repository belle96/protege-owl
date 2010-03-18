
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

/**
 * Interface representing OWL data value argument to atoms and built-ins
 */
public interface DataValueArgument extends BuiltInArgument, AtomArgument
{
	DataValue getDataValue();
}