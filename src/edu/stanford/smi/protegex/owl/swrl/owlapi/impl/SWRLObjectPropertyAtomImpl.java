
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLObjectPropertyAtom;

/**
 * Class representing a SWRL individuals atom
 */
public class SWRLObjectPropertyAtomImpl extends SWRLAtomImpl implements SWRLObjectPropertyAtom
{
  private String propertyURI;
  private SWRLArgument argument1, argument2;
  
  public SWRLObjectPropertyAtomImpl(String propertyURI)
  {
  	this.propertyURI = propertyURI;
    this.argument1 = null;
    this.argument2 = null;
  } 

  public SWRLObjectPropertyAtomImpl()
  {
    this.argument1 = null;
    this.argument2 = null;
  } 

  public int getNumberOfArguments() { return 2; }
  public String getPropertyURI() { return propertyURI; }    
  public void setArgument1(SWRLArgument argument1) { this.argument1 = argument1; }
  public void setArgument2(SWRLArgument argument2) { this.argument2 = argument2; }

  public SWRLArgument getFirstArgument() { return argument1; }
  public SWRLArgument getSecondArgument() { return argument2; }
  
  protected SWRLObjectPropertyAtomImpl(SWRLArgument argument1, SWRLArgument argument2)
  {
    this.argument1 = argument1;
    this.argument2 = argument2;
  }

}
