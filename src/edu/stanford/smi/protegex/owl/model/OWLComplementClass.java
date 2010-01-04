package edu.stanford.smi.protegex.owl.model;

/**
 * A OWLLogicalClass to represent a complement.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLComplementClass extends OWLLogicalClass {

    RDFSClass getComplement();


    void setComplement(RDFSClass complement);
}
