package edu.stanford.smi.protegex.owl.model;

import java.util.Collection;
import java.util.Set;


/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFSNamedClass extends RDFSClass, Deprecatable {

    /**
     * Creates an instance of this class so that Protege will recognize
     * this as an "anonymous" node in the RDF rendering.  Protege uses
     * an internal naming convention to simulate anonymous nodes.
     *
     * @return a new, anonymous instance of this
     * @see OWLModel#getNextAnonymousResourceName
     * @see OWLModel#isAnonymousResource
     */
    RDFResource createAnonymousInstance();


    /**
     * Creates a new individual of this (assuming this is not a metaclass).
     *
     * @param name the name of the new instance or null for a default value
     * @return the new instance
     */
    RDFIndividual createRDFIndividual(String name);


    /**
     * Gets all properties that have been associated with this class.
     * This includes all properties that have this in their union domain.
     * If this is an OWL class, then it also includes all domainless properties
     * that have been mentioned in any restriction on this class, except those
     * that have been restricted to a maximum cardinality of 0.
     * It also includes all subproperties of the aforementioned properties.
     * <p/>
     * This method is for example used to determine which properties shall appear
     * by default on a class form.
     *
     * @return the associated properties
     */
    Set getAssociatedProperties();


    /**
     * A convenience method to get the first direct superclass of this.
     * This method is typically used if it is known that there is only one parent,
     * e.g. in simple hierarchies.
     *
     * @return the first superclass
     */
    RDFSClass getFirstSuperclass();


    /**
     * Checks whether a given property is "functional" at this class.
     * A property is "functional" if it is declared to be owl:FunctionalProperty
     * or if this is an OWLNamedClass with a max cardinality restriction or 0 or 1.
     *
     * @param property the property to test
     * @return true  if property is functional at this class
     */
    boolean isFunctionalProperty(RDFProperty property);


    /**
     * Gets the allowed classes for a given property at this class.
     * This assumes that the property takes objects as values.
     * The method tests whether an allValuesFrom restriction has been
     * defined on this class, and resolves this into a collection if the
     * restriction has a union class as filler.
     * If no restriction could be found in the inheritance hierarchy, the
     * method looks for a global range restriction for the property.
     *
     * @param property the property to get the local range of
     * @return a Collection of RDFSClasses
     */
    Collection getUnionRangeClasses(RDFProperty property);


    /**
     * Checks whether this and a path to the root class is visible.
     *
     * @return true if visible
     */
    boolean isVisibleFromOWLThing();
}
