package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ResourceListener extends ProtegeFrameListener {

    /**
     * Called when a resource has been assigned a new rdf:type.
     *
     * @param resource the resource
     * @param type     the new rdf:type
     */
    void typeAdded(RDFResource resource, RDFSClass type);


    /**
     * Called when an rdf:type has been removed from a resource.
     *
     * @param resource the resource
     * @param type     the old rdf:type
     */
    void typeRemoved(RDFResource resource, RDFSClass type);
}
