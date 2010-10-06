
package edu.stanford.smi.protegex.owl.swrl.owlapi;


/**
 * Interface representing a SWRL data valued property atom
 */
public interface SWRLDataPropertyAtom extends SWRLAtom
{
  String getPropertyURI();
  SWRLArgument getFirstArgument();
  SWRLArgument getSecondArgument();
} 
