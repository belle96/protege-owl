
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLBinaryAtom;

public class SWRLBinaryAtomImpl extends SWRLAtomImpl implements SWRLBinaryAtom
{
  private String propertyURI;
  private SWRLArgument argument1, argument2;
  
  public SWRLBinaryAtomImpl(String propertyURI)
  {
  	this.propertyURI = propertyURI;
    this.argument1 = null;
    this.argument2 = null;
  } 

  public SWRLBinaryAtomImpl()
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
  
  protected SWRLBinaryAtomImpl(SWRLArgument argument1, SWRLArgument argument2)
  {
    this.argument1 = argument1;
    this.argument2 = argument2;
  }

}
