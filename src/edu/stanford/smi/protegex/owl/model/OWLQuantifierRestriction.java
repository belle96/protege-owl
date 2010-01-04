package edu.stanford.smi.protegex.owl.model;


/**
 * The common base interface of OWLAllValuesFrom and OWLSomeValuesFrom.
 * This basically defines the shared operations on both kinds of Restrictions.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLQuantifierRestriction extends OWLRestriction {

    /**
     * Gets the filler of this restriction, which is either an RDFSDatatype,
     * an OWLDataRange, or a RDFSClass.
     *
     * @return the filler
     */
    RDFResource getFiller();


    /**
     * Sets the filler of this restriction.
     *
     * @param filler an RDFSDatatype, OWLDataRange, or a RDFSClass
     */
    void setFiller(RDFResource filler);
}
