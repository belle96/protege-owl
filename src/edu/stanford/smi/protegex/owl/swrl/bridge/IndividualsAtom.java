
package edu.stanford.smi.protegex.owl.swrl.bridge;

/*
** Interface representing a SWRL individuals atom
*/
public interface IndividualsAtom extends Atom
{
  AtomArgument getArgument1();
  AtomArgument getArgument2();
} // IndividualsAtom

