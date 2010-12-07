
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLSameIndividualAtom;

/**
 * Class representing a SWRL same individual atom
 */
public class SWRLSameIndividualAtomImpl extends SWRLBinaryAtomImpl implements SWRLSameIndividualAtom
{
  public SWRLSameIndividualAtomImpl(SWRLArgument argument1, SWRLArgument argument2) { super(argument1, argument2); }

  public SWRLSameIndividualAtomImpl() { super(); }

  public String toString() { return "sameAs(" + getFirstArgument() + ", " + getSecondArgument() + ")"; }
}
