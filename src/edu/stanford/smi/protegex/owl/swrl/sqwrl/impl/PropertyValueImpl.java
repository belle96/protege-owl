package edu.stanford.smi.protegex.owl.swrl.sqwrl.impl;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.PropertyValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResultValue;

public abstract class PropertyValueImpl implements PropertyValue
{
	private String propertyURI;
	
	public PropertyValueImpl(String propertyURI) { this.propertyURI = propertyURI; }
	
	public String getURI() { return propertyURI; }
	
	public int compareTo(SQWRLResultValue o)
  {
    return getURI().compareTo(((PropertyValueImpl)o).getURI());
  } // compareTo
 
}
