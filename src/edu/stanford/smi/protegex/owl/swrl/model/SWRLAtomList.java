package edu.stanford.smi.protegex.owl.swrl.model;

import edu.stanford.smi.protegex.owl.model.RDFList;

/**
 * @author Martin O'Connor  <moconnor@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SWRLAtomList extends RDFList, SWRLIndividual {
  void setInHead(boolean isInHead);
} // SWRLAtomList

