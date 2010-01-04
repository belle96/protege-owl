package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ClassListener extends ProtegeClsListener {

    /**
     * Called after a class has been added to the union domain of a property.
     *
     * @param cls      the class that was added
     * @param property the property that has changed its domain
     */
    void addedToUnionDomainOf(RDFSClass cls, RDFProperty property);


    /**
     * Called after a (new) resource was made an instance of a class.
     *
     * @param cls      the RDFSClass of the instance
     * @param instance the instance that was added
     */
    void instanceAdded(RDFSClass cls, RDFResource instance);


    /**
     * Called after a resource was removed from the instances of a class.
     *
     * @param cls      the RDFSClass of the instance
     * @param instance the instance that was removed
     */
    void instanceRemoved(RDFSClass cls, RDFResource instance);


    /**
     * Called after a class has been removed from the union domain of a property.
     *
     * @param cls      the class that was removed
     * @param property the property that has changed its domain
     */
    void removedFromUnionDomainOf(RDFSClass cls, RDFProperty property);


    /**
     * Called after a class has been added as a subclass to another class.
     *
     * @param cls      the class that was changed
     * @param subclass the new subclass of cls
     */
    void subclassAdded(RDFSClass cls, RDFSClass subclass);


    /**
     * Called after a class has been removed from the subclasses of another class.
     *
     * @param cls      the class that was changed
     * @param subclass the former subclass of cls
     */
    void subclassRemoved(RDFSClass cls, RDFSClass subclass);


    /**
     * Called after a class has been added as a superclass to another class.
     *
     * @param cls        the class that was changed
     * @param superclass the new superclass of cls
     */
    void superclassAdded(RDFSClass cls, RDFSClass superclass);


    /**
     * Called after a class has been removed from the superclasses of another class.
     *
     * @param cls        the class that was changed
     * @param superclass the former superclass of cls
     */
    void superclassRemoved(RDFSClass cls, RDFSClass superclass);
}
