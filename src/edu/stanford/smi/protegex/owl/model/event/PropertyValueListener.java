package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protege.event.FrameListener;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface PropertyValueListener extends FrameListener {

    /**
     * Called when the browser text of an RDFResource has changed.
     * This is typically used to trigger repaints in a user interface.
     *
     * @param resource the resource that has changed its browser text
     */
    void browserTextChanged(RDFResource resource);


    /**
     * Called after the name of a resource has changed.
     *
     * @param resource the resource that has changed its name
     * @param oldName  the old name of the resource
     */
    void nameChanged(RDFResource resource, String oldName);


    /**
     * Called after the value of a property has changed at a given resource.
     *
     * @param resource  the resource that has changed its value
     * @param property  the property that has changed at resource
     * @param oldValues a Collection of old values (may not be available for all values)
     */
    void propertyValueChanged(RDFResource resource, RDFProperty property, Collection oldValues);


    /**
     * Called after the visibility of a resource has changed.
     *
     * @param resource the resource that has changed its visibility
     */
    void visibilityChanged(RDFResource resource);
}
