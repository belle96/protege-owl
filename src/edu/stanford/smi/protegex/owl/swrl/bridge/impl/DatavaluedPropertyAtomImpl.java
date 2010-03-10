
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.AtomArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DatavaluedPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

/**
 * Class representing a SWRL data valued property atom
 */
public class DatavaluedPropertyAtomImpl extends AtomImpl implements DatavaluedPropertyAtom
{
  private String propertyURI;
  private AtomArgument argument1, argument2;
  
  public DatavaluedPropertyAtomImpl(String propertyURI, AtomArgument argument1, AtomArgument argument2)
  {
    this.propertyURI = propertyURI;
    this.argument1 = argument1;
    this.argument2 = argument2;
  }

  public DatavaluedPropertyAtomImpl(String propertyURI)
  {
    this.propertyURI = propertyURI;
    this.argument1 = null;
    this.argument2 = null;
  }
  
  public int getNumberOfArguments() { return 2; }

  public void setArgument1(AtomArgument argument1) { this.argument1 = argument1; }
  public void setArgument2(AtomArgument argument2) { this.argument2 = argument2; }

  public String getPropertyURI() { return propertyURI; }   
  public AtomArgument getArgument1() { return argument1; }
  public AtomArgument getArgument2() { return argument2; }

  public String toString() 
  { 
    String result = "" + getPropertyURI() + "(" + getArgument1() + ", ";

    if (getArgument2() instanceof DataValueArgument) {
    	DataValueArgument dataValueArgument = (DataValueArgument)getArgument2();
    	DataValue dataValue = dataValueArgument.getDataValue();
    	if (dataValue.isString()) result += "\"" + dataValue + "\"";
    	else result += "" + dataValue;
    } else result += "" + getArgument2();

    result += ")"; 

    return result;
  } 
} 
