
package edu.stanford.smi.protegex.owl.swrl.bridge;

/*
** Interface representing a SWRL individual property atom
*/
public interface IndividualPropertyAtom extends Atom
{
  String getPropertyName();
  String getPrefixedPropertyName();
  AtomArgument getArgument1();
  AtomArgument getArgument2();
} // IndividualPropertyAtom
