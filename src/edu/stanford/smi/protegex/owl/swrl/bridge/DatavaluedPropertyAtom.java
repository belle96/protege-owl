
package edu.stanford.smi.protegex.owl.swrl.bridge;

/**
 * Interface representing a SWRL data valued property atom
 */
public interface DatavaluedPropertyAtom extends SWRLAtom
{
  String getPropertyURI();
  AtomArgument getArgument1();
  AtomArgument getArgument2();
} 
