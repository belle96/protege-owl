
package edu.stanford.smi.protegex.owl.swrl.bridge;

public interface ClassAtom extends Atom
{
  String getClassURI();
  String getPrefixedClassName();
  AtomArgument getArgument1();
} // ClassAtom
