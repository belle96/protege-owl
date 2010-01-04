package edu.stanford.smi.protegex.owl.swrl.model;


/**
 * The base interface of the various atom types in SWRL.
 * Since atoms (like most other SWRL language elements) are represented
 * as OWL individuals, this interface inherits from RDFIndividual.
 *
 * @author Martin O'Connor  <moconnor@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SWRLAtom extends SWRLIndividual {

} // SWRLAtom

