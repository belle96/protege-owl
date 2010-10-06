
package edu.stanford.smi.protegex.owl.swrl.bridge;

/*
** Interface representing a SWRL individuals atom
*/
public interface IndividualsAtom extends SWRLAtom
{
  AtomArgument getArgument1();
  AtomArgument getArgument2();
} // IndividualsAtom

