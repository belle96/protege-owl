
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.AtomArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualPropertyAtom;

/*
** Class representing a SWRL individual property atom
*/
public class IndividualPropertyAtomImpl extends AtomImpl implements IndividualPropertyAtom
{
  private String propertyName, prefixedPropertyName;
  private AtomArgument argument1, argument2;

  public IndividualPropertyAtomImpl(String propertyName, String prefixedPropertyName, AtomArgument argument1, AtomArgument argument2)
  {
    this.propertyName = propertyName;
    this.prefixedPropertyName = prefixedPropertyName;
    this.argument1 = argument1;
    this.argument2 = argument2;
  } // IndividualPropertyAtomImpl

  public IndividualPropertyAtomImpl(String propertyName, String prefixedPropertyName)
  {
    this.propertyName = propertyName;
    this.prefixedPropertyName = prefixedPropertyName;
    this.argument1 = null;
    this.argument2 = null;
  } // IndividualPropertyAtomImpl
    
  public void setArgument1(AtomArgument argument1) { this.argument1 = argument1; }
  public void setArgument2(AtomArgument argument2) { this.argument2 = argument2; }

  public String getPropertyName() { return propertyName; }  
  public String getPrefixedPropertyName() { return prefixedPropertyName; }  
  public AtomArgument getArgument1() { return argument1; }
  public AtomArgument getArgument2() { return argument2; }  

  public String toString() { return getPrefixedPropertyName() + "(" + getArgument1() + ", " + getArgument2() + ")"; }
} // IndividualPropertyAtomImpl
