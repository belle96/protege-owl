
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLDataPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLLiteralArgument;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

/**
 * Class representing a SWRL data valued property atom
 */
public class SWRLDataPropertyAtomImpl extends SWRLAtomImpl implements SWRLDataPropertyAtom
{
  private String propertyURI;
  private SWRLArgument argument1, argument2;
  
  public SWRLDataPropertyAtomImpl(String propertyURI, SWRLArgument argument1, SWRLArgument argument2)
  {
    this.propertyURI = propertyURI;
    this.argument1 = argument1;
    this.argument2 = argument2;
  }

  public SWRLDataPropertyAtomImpl(String propertyURI)
  {
    this.propertyURI = propertyURI;
    this.argument1 = null;
    this.argument2 = null;
  }
  
  public int getNumberOfArguments() { return 2; }

  public void setArgument1(SWRLArgument argument1) { this.argument1 = argument1; }
  public void setArgument2(SWRLArgument argument2) { this.argument2 = argument2; }

  public String getPropertyURI() { return propertyURI; }   
  public SWRLArgument getFirstArgument() { return argument1; }
  public SWRLArgument getSecondArgument() { return argument2; }

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
