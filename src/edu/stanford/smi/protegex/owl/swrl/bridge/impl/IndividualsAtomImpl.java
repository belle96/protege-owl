
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.*;

/*
** Class representing a SWRL individuals atom
*/
public abstract class IndividualsAtomImpl extends AtomImpl implements IndividualsAtom
{
  private AtomArgument argument1, argument2;
  
  public IndividualsAtomImpl(AtomArgument argument1, AtomArgument argument2)
  {
    this.argument1 = argument1;
    this.argument2 = argument2;
  } // IndividualsAtomImpl

  public IndividualsAtomImpl()
  {
    this.argument1 = null;
    this.argument2 = null;
  } // IndividualsAtomImpl

  public void setArgument1(AtomArgument argument1) { this.argument1 = argument1; }
  public void setArgument2(AtomArgument argument2) { this.argument2 = argument2; }

  public AtomArgument getArgument1() { return argument1; }
  public AtomArgument getArgument2() { return argument2; }
} // IndividualsAtomImpl
