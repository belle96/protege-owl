
package edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.AtomArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.SameIndividualAtom;

/*
** Class representing a SWRL same individual atom
*/
public class SameIndividualAtomImpl extends IndividualsAtomImpl implements SameIndividualAtom
{
  public SameIndividualAtomImpl(AtomArgument argument1, AtomArgument argument2) { super(argument1, argument2); }

  public SameIndividualAtomImpl() { super(); }

  public String toString() { return "sameAs(" + getArgument1() + ", " + getArgument2() + ")"; }
} // SameIndividualAtomImpl
