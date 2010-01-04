package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protege.event.FrameAdapter;
import edu.stanford.smi.protege.event.FrameEvent;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyValueAdapter extends FrameAdapter implements PropertyValueListener {

    public void propertyValueChanged(RDFResource resource, RDFProperty property, Collection oldValues, FrameEvent event) {
    	propertyValueChanged(resource, property, oldValues);
    }

    public void propertyValueChanged(RDFResource resource, RDFProperty property, Collection oldValues) {
        // Do nothing
    }

    public void browserTextChanged(RDFResource resource, FrameEvent event) {
    	browserTextChanged(resource);
    }
    
    public void browserTextChanged(RDFResource resource) {
        // Do nothing
    }

    public void resourceReplaced(RDFResource oldResource, RDFResource newResource, String oldName) {
		nameChanged(oldResource, oldName);
	} 
    
    public void nameChanged(RDFResource resource, String oldName) {
        // Do nothing
    }

    public void visibilityChanged(RDFResource resource, FrameEvent event) {
    	visibilityChanged(resource);
    }
    
    public void visibilityChanged(RDFResource resource) {
        // Do nothing
    }
    
    
    /************* Deprecated methods **************/

    /**
     * @deprecated
     */    
    public final void browserTextChanged(FrameEvent event) {
        if (event.getFrame() instanceof RDFResource) {
            browserTextChanged((RDFResource) event.getFrame(), event);
        }
    }
    

    /**
     * @deprecated
     */
    public final void nameChanged(FrameEvent event) {
        if (event.getFrame() instanceof RDFResource) {
            resourceReplaced((RDFResource) event.getFrame(), (RDFResource) event.getNewFrame(),  event.getOldName());
        }
    }

    
    /**
     * @deprecated
     */
    public final void ownSlotValueChanged(FrameEvent event) {
        if (event.getFrame() instanceof RDFResource && event.getSlot() instanceof RDFProperty) {
            propertyValueChanged((RDFResource) event.getFrame(), (RDFProperty) event.getSlot(), event.getOldValues(),event);
        }
    }

    /**
     * @deprecated
     */
    public final void visibilityChanged(FrameEvent event) {
        if (event.getFrame() instanceof RDFResource) {
            visibilityChanged((RDFResource) event.getFrame(),event);
        }
    }
    
    
    /**
     * @deprecated
     */
    public final void deleted(FrameEvent event) {
    }
    
    
    /**
     * @deprecated
     */
    public final void ownFacetAdded(FrameEvent event) {
    }


    /**
     * @deprecated
     */
    public final void ownFacetRemoved(FrameEvent event) {
    }


    /**
     * @deprecated
     */
    public final void ownFacetValueChanged(FrameEvent event) {
    }


    /**
     * @deprecated
     */
    public final void ownSlotAdded(FrameEvent event) {
    }


    /**
     * @deprecated
     */
    public final void ownSlotRemoved(FrameEvent event) {
    }



}
