
package edu.stanford.smi.protegex.owl.swrl.bridge.tmp;

import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl.BuiltInArgumentImpl;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

/**
 * Implementation of an DataValue object that represents Java and XML Schema primitive data literals.
 */
public class DataValueArgumentImpl extends BuiltInArgumentImpl implements DataValueArgument 
{
	private DataValue dataValue;
	
	public DataValueArgumentImpl(DataValue dataValue) { this.dataValue = dataValue; }
	
	public DataValue getDataValue() { return dataValue; }
	
	public int compareTo(DataValueArgument argument) { return dataValue.compareTo(argument.getDataValue()); }
} // DataValueArgumentImpl
