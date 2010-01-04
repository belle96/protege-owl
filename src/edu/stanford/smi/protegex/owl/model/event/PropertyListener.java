package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * A listener for property-related events.
 * Note that this currently extends ProtegeSlotListener for technical
 * reasons, but none of the inherited methods from SlotListener
 * should be used in application code.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface PropertyListener extends ProtegeSlotListener {

    /**
     * Called after a subproperty has been added to a property.
     *
     * @param property    the property where the subproperty was added
     * @param subproperty the new subproperty
     */
    void subpropertyAdded(RDFProperty property, RDFProperty subproperty);


    /**
     * Called after a subproperty has been removed from a property.
     *
     * @param property    the property where the subproperty was removed
     * @param subproperty the old subproperty
     */
    void subpropertyRemoved(RDFProperty property, RDFProperty subproperty);


    /**
     * Called after a superproperty has been added to a property.
     *
     * @param property      the property where the superproperty was added
     * @param superproperty the new superproperty
     */
    void superpropertyAdded(RDFProperty property, RDFProperty superproperty);


    /**
     * Called after a superproperty has been removed from a property.
     *
     * @param property      the property where the superproperty was removed
     * @param superproperty the old superproperty
     */
    void superpropertyRemoved(RDFProperty property, RDFProperty superproperty);


    /**
     * Called after a class has been added to the union domain of a property.
     *
     * @param property  the property where the domain has changed
     * @param rdfsClass the domain class that was added
     */
    void unionDomainClassAdded(RDFProperty property, RDFSClass rdfsClass);


    /**
     * Called after a class has been removed from the union domain of a property.
     *
     * @param property  the property where the domain has changed
     * @param rdfsClass the domain class that was removed
     */
    void unionDomainClassRemoved(RDFProperty property, RDFSClass rdfsClass);
}
