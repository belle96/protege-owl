package edu.stanford.smi.protegex.owl.model;

/**
 * The base interface of the various cardinality restrictions.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLCardinalityBase extends OWLRestriction {

    /**
     * Gets the cardinality value in this restriction.
     *
     * @return a positive integer
     */
    int getCardinality();


    /**
     * Gets the qualifier class.  If this is a qualified cardinality restriction, then
     * this is the value of the owl:valuesFrom property.  Otherwise, this method returns
     * owl:Thing.
     *
     * @return owl:Thing or the result of <CODE>getValuesFrom()</CODE>.
     */
    RDFSClass getQualifier();


    /**
     * If this is a qualified cardinality restriction, then this gets the
     * owl:valuesFrom property value.
     *
     * @return the qualifier class or null if this is not a qualified cardinality restriction
     */
    RDFSClass getValuesFrom();


    /**
     * Checks if this is a qualified cardinality restriction.
     * This is true if this has a value for the owl:valuesFrom property.
     *
     * @return true  if this is a qualified cardinality restriction
     */
    boolean isQualified();


    /**
     * Sets the cardinality value in this restriction.
     *
     * @param value the new cardinality value
     */
    void setCardinality(int value);


    void setValuesFrom(RDFSClass value);
}
