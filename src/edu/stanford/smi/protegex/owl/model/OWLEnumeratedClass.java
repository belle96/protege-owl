package edu.stanford.smi.protegex.owl.model;

import java.util.Collection;
import java.util.Iterator;

/**
 * An enumerated class which lists valid individuals as its values of
 * the owl:oneOf property.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLEnumeratedClass extends OWLAnonymousClass {

    /**
     * Adds a resource to this enumeration.
     *
     * @param resource the RDFResource to add (typically individuals)
     */
    void addOneOf(RDFResource resource);


    /**
     * Gets the values of the owl:oneOf property at this, i.e. the
     * resources that are part of this enumeration.
     *
     * @return the values of owl:oneOf (a Collection of RDFResources)
     */
    Collection getOneOf();


    /**
     * @deprecated use getOneOf instead
     */
    Collection getOneOfValues();


    /**
     * Gets an Iterator of the values in the owl:oneOf list.
     * @return an Iterator of RDFResources
     */
    Iterator listOneOf();


    /**
     * Removes a resource from this enumeration.
     *
     * @param resource the resource to remove
     */
    void removeOneOf(RDFResource resource);


    /**
     * Sets the values of the owl:oneOf property at this.
     *
     * @param resources a Collection of RDFResources
     */
    void setOneOf(Collection resources);


    /**
     * @deprecated use setOneOf instead
     */
    void setOneOfValues(Collection values);
}
