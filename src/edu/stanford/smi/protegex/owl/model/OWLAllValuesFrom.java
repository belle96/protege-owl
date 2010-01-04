package edu.stanford.smi.protegex.owl.model;


/**
 * An owl:allValueFrom restriction.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLAllValuesFrom extends OWLQuantifierRestriction {

    /**
     * Gets the owl:allValuesFrom filler.
     *
     * @return the filler
     */
    RDFResource getAllValuesFrom();


    /**
     * Sets the owl:allValuesFrom filler.
     *
     * @param allValuesFrom the new filler
     */
    void setAllValuesFrom(RDFResource allValuesFrom);
}
