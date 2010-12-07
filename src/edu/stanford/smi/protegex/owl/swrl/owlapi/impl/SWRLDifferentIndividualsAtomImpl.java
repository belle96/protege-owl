
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLDifferentIndividualsAtom;

/**
 * Class representing a SWRL different individual atom
 */
public class SWRLDifferentIndividualsAtomImpl extends SWRLBinaryAtomImpl implements SWRLDifferentIndividualsAtom
{
  public SWRLDifferentIndividualsAtomImpl(SWRLArgument argument1, SWRLArgument argument2) { super(argument1, argument2); }
  public SWRLDifferentIndividualsAtomImpl() { super(); }

  public String toString() { return "differentFrom(" + getFirstArgument() + ", " + getSecondArgument() + ")"; }
}
