package edu.stanford.smi.protegex.owl.swrl.model;

import edu.stanford.smi.protegex.owl.model.RDFList;

/**
 * @author Martin O'Connor  <moconnor@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SWRLBuiltinAtom extends SWRLAtom 
{
  RDFList getArguments();
  void setArguments(RDFList arguments);
  SWRLBuiltin getBuiltin();
  void setBuiltin(SWRLBuiltin swrlBuiltin); 
} // SWRLBuiltinAtom

