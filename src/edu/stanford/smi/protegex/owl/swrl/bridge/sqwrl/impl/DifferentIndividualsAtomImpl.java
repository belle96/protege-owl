
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.AtomArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DifferentIndividualsAtom;

/*
** Class representing a SWRL same individual atom
*/
public class DifferentIndividualsAtomImpl extends IndividualsAtomImpl implements DifferentIndividualsAtom
{
  public DifferentIndividualsAtomImpl(AtomArgument argument1, AtomArgument argument2) { super(argument1, argument2); }
  public DifferentIndividualsAtomImpl() { super(); }

  public String toString() { return "differentFrom(" + getArgument1() + ", " + getArgument2() + ")"; }
} // DifferentIndividualsAtomImpl
