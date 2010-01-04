
package edu.stanford.smi.protegex.owl.swrl.bridge;

public interface ClassAtom extends Atom
{
  String getClassName();
  String getPrefixedClassName();
  AtomArgument getArgument1();
} // ClassAtom
