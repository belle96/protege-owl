
package edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.*;

/**
 * Class representing a SWRL class atom
 */
public class ClassAtomImpl extends AtomImpl implements ClassAtom
{
  private AtomArgument argument1;
  private String classURI;

  public ClassAtomImpl(String classURI, AtomArgument argument1)
  {
    this.classURI = classURI;
    this.argument1 = argument1;
  } // ClassAtomImpl

  public ClassAtomImpl(String classURI)
  {
    this.classURI = classURI;
    this.argument1 = null;
  } // ClassAtomImpl

  public void setArgument1(AtomArgument argument1) { this.argument1 = argument1; }
    
  public String getClassURI() { return classURI; }
  public AtomArgument getArgument1() { return argument1; }

  public String toString() { return getClassURI() + "(" + getArgument1() + ")"; }
} // ClassAtomImpl

