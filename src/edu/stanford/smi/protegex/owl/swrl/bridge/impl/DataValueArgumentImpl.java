
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

/**
 * Implementation of an DataValue object that represents Java and XML Schema primitive data literals.
 */
public class DataValueArgumentImpl extends BuiltInArgumentImpl implements DataValueArgument 
{
	private DataValue dataValue;
	
	public DataValueArgumentImpl(DataValue dataValue) { this.dataValue = dataValue; }
	
	public DataValue getDataValue() { return dataValue; }
	
	public String toString() { return dataValue.toString(); }
	
	public int compareTo(DataValueArgument argument) { return dataValue.compareTo(argument.getDataValue()); }
	
	public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    DataValueArgumentImpl impl = (DataValueArgumentImpl)obj;
    return (getDataValue() == impl.getDataValue() || (getDataValue() != null && getDataValue().equals(impl.getDataValue())));
    } // equals

  public int hashCode()
  {
    int hash = 12;
    hash = hash + (null == getDataValue() ? 0 : getDataValue().hashCode());   
    return hash;
  } // hashCode

} // DataValueArgumentImpl
