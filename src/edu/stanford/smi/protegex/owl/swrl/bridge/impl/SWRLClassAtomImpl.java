
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLClassAtom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.SWRLAtomImpl;

/**
 * Class representing a SWRL class atom
 */
public class SWRLClassAtomImpl extends SWRLAtomImpl implements SWRLClassAtom
{
  private SWRLArgument argument1;
  private String classURI;

  public SWRLClassAtomImpl(String classURI, SWRLArgument argument1)
  {
    this.classURI = classURI;
    this.argument1 = argument1;
  }

  public SWRLClassAtomImpl(String classURI)
  {
    this.classURI = classURI;
    this.argument1 = null;
  }

  public int getNumberOfArguments() { return 1; }
  
  public void setArgument1(SWRLArgument argument1) { this.argument1 = argument1; }
    
  public String getClassURI() { return classURI; }
  public SWRLArgument getArgument1() { return argument1; }

  public String toString() { return getClassURI() + "(" + getArgument1() + ")"; }
}

