package edu.stanford.smi.protegex.owl.model;


/**
 * A some-values-from OWLRestriction.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLSomeValuesFrom extends OWLQuantifierRestriction, OWLExistentialRestriction {

    /**
     * Gets the owl:someValuesFrom filler.
     *
     * @return the filler
     */
    RDFResource getSomeValuesFrom();


    /**
     * Sets the owl:someValuesFrom filler.
     *
     * @param someValuesFrom the new filler
     */
    void setSomeValuesFrom(RDFResource someValuesFrom);
}
