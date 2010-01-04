package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ModelListener extends ProtegeKnowledgeBaseListener {

    /**
     * Called after a new RDFSClass has been created.
     *
     * @param cls the new class
     */
    void classCreated(RDFSClass cls);


    /**
     * Called after an RDFSClass has been deleted.
     *
     * @param cls the deleted class
     */
    void classDeleted(RDFSClass cls);


    /**
     * Called after an individual has been created.
     *
     * @param resource the new resource
     */
    void individualCreated(RDFResource resource);


    /**
     * Called after an individual has been deleted.
     *
     * @param resource the old resource
     */
    void individualDeleted(RDFResource resource);


    /**
     * Called after a property has been created.
     *
     * @param property the new property
     */
    void propertyCreated(RDFProperty property);


    /**
     * Called after a property has been deleted.
     *
     * @param property the old property
     */
    void propertyDeleted(RDFProperty property);


    /**
     * Called after the name of a resource has changed.
     *
     * @param resource the resource that changed its name
     * @param oldName  the old name of the resource
     */
    void resourceNameChanged(RDFResource resource, String oldName);
}
