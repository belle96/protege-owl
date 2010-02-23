
package edu.stanford.smi.protegex.owl.swrl.bridge;

/**
 * Interface representing a SWRL individual property atom
 */
public interface IndividualPropertyAtom extends Atom
{
  String getPropertyURI();
  AtomArgument getArgument1();
  AtomArgument getArgument2();
} // IndividualPropertyAtom
