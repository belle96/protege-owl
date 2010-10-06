
package edu.stanford.smi.protegex.owl.swrl.owlapi;


/**
 * Interface representing a SWRL individuals atom
 */
public interface SWRLObjectPropertyAtom extends SWRLAtom
{
  SWRLArgument getFirstArgument();
  SWRLArgument getSecondArgument();
  String getPropertyURI();
}

