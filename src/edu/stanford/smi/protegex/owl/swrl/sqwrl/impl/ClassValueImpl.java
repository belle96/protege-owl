
package edu.stanford.smi.protegex.owl.swrl.sqwrl.impl;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.ClassValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResultValue;

public class ClassValueImpl implements ClassValue
{
	private String classURI;
	
	public ClassValueImpl(String classURI) { this.classURI = classURI; }
	
	public String getURI() { return classURI; }
	
	public int compareTo(SQWRLResultValue o)
  {
    return classURI.compareTo(((ClassValueImpl)o).getURI());
  } // compareTo

}
