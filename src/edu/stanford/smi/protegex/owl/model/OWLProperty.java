package edu.stanford.smi.protegex.owl.model;


/**
 * The common base interface of OWLDatatypeProperty and OWLObjectProperty.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLProperty extends RDFProperty {


    /**
     * Checks whether this is an inverse functional property.
     * This is true if this either has the rdf:type owl:InverseFunctionalProperty
     * or one of its super properties is inverse functional.
     *
     * @return true   if this is inverse functional
     */
    boolean isInverseFunctional();


    /**
     * Checks whether this is an object slot or a datatype slot.
     * This method is probably hardly ever needed - it is for the
     * case where instanceof fails because a property has just changed
     * from datatype to object property and the Java object still has
     * the old type.
     *
     * @return true  if this is an Object property
     */
    boolean isObjectProperty();


    void setInverseFunctional(boolean value);
}
