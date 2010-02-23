package edu.stanford.smi.protegex.owl.swrl.sqwrl.impl;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.IndividualValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResultValue;

public class IndividualValueImpl implements IndividualValue
{
	private String individualURI;
	
	public IndividualValueImpl(String individualURI) { this.individualURI = individualURI; }
	
	public String getURI() { return individualURI; }
	
	public int compareTo(SQWRLResultValue o)
  {
    return getURI().compareTo(((IndividualValueImpl)o).getURI());
  } // compareTo
 
}
