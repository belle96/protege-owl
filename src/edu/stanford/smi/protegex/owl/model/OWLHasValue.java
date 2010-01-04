package edu.stanford.smi.protegex.owl.model;


/**
 * A class representing an owl:hasValue restriction.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLHasValue extends OWLExistentialRestriction {

    /**
     * Gets the owl:hasValue value of this restriction.
     *
     * @return either an RDFResource, an RDFSLiteral or a primitive value
     * @see #setHasValue
     */
    Object getHasValue();


    /**
     * Sets the owl:hasValue of this restriction.
     *
     * @param value either an RDFResource, an RDFSLiteral or a primitive
     *              value (String, Float, Integer, or Boolean)
     */
    void setHasValue(Object value);
}
