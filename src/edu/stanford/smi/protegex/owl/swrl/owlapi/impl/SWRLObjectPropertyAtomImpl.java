
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLObjectPropertyAtom;

public class SWRLObjectPropertyAtomImpl extends SWRLBinaryAtomImpl implements SWRLObjectPropertyAtom
{
	private String propertyURI;

	public SWRLObjectPropertyAtomImpl(String propertyURI, SWRLArgument argument1, SWRLArgument argument2)
	{
		super(argument1, argument2);
	  this.propertyURI = propertyURI;
	}

	public SWRLObjectPropertyAtomImpl(String propertyURI)
	{
	  this.propertyURI = propertyURI;
	}

	public String getPropertyURI() { return propertyURI; }   

	public String toString() 
	{ 
	  return getPropertyURI() + "(" + getFirstArgument() + ", " + getSecondArgument() + ")";
	} 

}
