package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protege.event.KnowledgeBaseEvent;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ModelAdapter implements ModelListener {

    public void classCreated(RDFSClass cls, KnowledgeBaseEvent event) {
    	classCreated(cls);
    }

	
    public void classCreated(RDFSClass cls) {
        // Do nothing
    }
    

    public void classDeleted(RDFSClass cls, KnowledgeBaseEvent event) {
    	classDeleted(cls);
    }


    public void classDeleted(RDFSClass cls) {
        // Do nothing
    }


    public void propertyCreated(RDFProperty property, KnowledgeBaseEvent event) {
    	propertyCreated(property);
    }

    
    public void propertyCreated(RDFProperty property) {
        // Do nothing
    }

    public void propertyDeleted(RDFProperty property, KnowledgeBaseEvent event) {
    	propertyDeleted(property);
    }

    
    public void propertyDeleted(RDFProperty property) {
        // Do nothing
    }


    public void individualCreated(RDFResource resource, KnowledgeBaseEvent event) {
    	individualCreated(resource);
    }
    
    public void individualCreated(RDFResource resource) {
        // Do nothing
    }


    public void individualDeleted(RDFResource resource, KnowledgeBaseEvent event) {
    	individualDeleted(resource);
    }

    
    public void individualDeleted(RDFResource resource) {
        // Do nothing
    }

    
    public void resourceReplaced(RDFResource oldResource, RDFResource newResource, String oldName) {    	
		resourceNameChanged(oldResource, oldName);
	}    
    

    /************* Deprecated methods **************/
    
    /**
     * @deprecated Use {@link #resourceReplaced(RDFResource, RDFResource, String)}
     */
    public void resourceNameChanged(RDFResource resource, String oldName) {
        // Do nothing
    }
    
    /**
     * @deprecated
     */
    public final void clsCreated(KnowledgeBaseEvent event) {
        if (event.getCls() instanceof RDFSClass) {
            classCreated((RDFSClass) event.getCls(), event);
        }
    }


    /**
     * @deprecated
     */
    public final void clsDeleted(KnowledgeBaseEvent event) {
        if (event.getCls() instanceof RDFSClass) {
            classDeleted((RDFSClass) event.getCls(), event);
        }
    }


    /**
     * @deprecated
     */
    public final void frameNameChanged(KnowledgeBaseEvent event) {
        if (event.getFrame() instanceof RDFResource) {        	
        	resourceReplaced((RDFResource) event.getFrame(),(RDFResource) event.getNewFrame(), event.getOldName());           
        }
    }


	/**
     * @deprecated
     */
    public final void instanceCreated(KnowledgeBaseEvent event) {
        if (event.getFrame() instanceof RDFResource) {
            individualCreated((RDFResource) event.getFrame(), event);
        }
    }


    /**
     * @deprecated
     */
    public final void instanceDeleted(KnowledgeBaseEvent event) {
        if (event.getFrame() instanceof RDFResource) {
            individualDeleted((RDFResource) event.getFrame(), event);
        }
    }
    
    
    /**
     * @deprecated
     */
    public final void slotCreated(KnowledgeBaseEvent event) {
        if (event.getSlot() instanceof RDFProperty) {
            propertyCreated((RDFProperty) event.getSlot(), event);
        }
    }


    /**
     * @deprecated
     */
    public final void slotDeleted(KnowledgeBaseEvent event) {
        if (event.getSlot() instanceof RDFProperty) {
            propertyDeleted((RDFProperty) event.getSlot(), event);
        }
    }
    
    /**
     * @deprecated
     */
    public final void defaultClsMetaClsChanged(KnowledgeBaseEvent event) {
    }


    /**
     * @deprecated
     */
    public final void defaultFacetMetaClsChanged(KnowledgeBaseEvent event) {
    }


    /**
     * @deprecated
     */
    public final void defaultSlotMetaClsChanged(KnowledgeBaseEvent event) {
    }


    /**
     * @deprecated
     */
    public final void facetCreated(KnowledgeBaseEvent event) {
    }


    /**
     * @deprecated
     */
    public final void facetDeleted(KnowledgeBaseEvent event) {
    }
    
}
