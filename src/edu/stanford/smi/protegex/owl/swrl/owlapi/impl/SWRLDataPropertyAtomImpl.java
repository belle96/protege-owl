
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLDataPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLLiteralArgument;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

/**
 * Class representing a SWRL data valued property atom
 */
public class SWRLDataPropertyAtomImpl extends SWRLBinaryAtomImpl implements SWRLDataPropertyAtom
{
  private String propertyURI;
  
  public SWRLDataPropertyAtomImpl(String propertyURI, SWRLArgument argument1, SWRLArgument argument2)
  {
  	super(argument1, argument2);
    this.propertyURI = propertyURI;
  }

  public SWRLDataPropertyAtomImpl(String propertyURI)
  {
    this.propertyURI = propertyURI;
  }
  
  public String getPropertyURI() { return propertyURI; }   

  public String toString() 
  { 
    String result = "" + getPropertyURI() + "(" + getFirstArgument() + ", ";

    if (getSecondArgument() instanceof SWRLLiteralArgument) {
    	SWRLLiteralArgument dataValueArgument = (SWRLLiteralArgument)getSecondArgument();
    	DataValue dataValue = dataValueArgument.getLiteral();
    	if (dataValue.isString()) result += "\"" + dataValue + "\"";
    	else result += "" + dataValue;
    } else result += "" + getSecondArgument();

    result += ")"; 

    return result;
  } 
} 
