package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protege.event.ClsEvent;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassAdapter implements ClassListener {

	public void addedToUnionDomainOf(RDFSClass cls, RDFProperty property, ClsEvent event) {
		addedToUnionDomainOf(cls, property);
    }
	
    public void addedToUnionDomainOf(RDFSClass cls, RDFProperty property) {
        // Do nothing
    }

    public void removedFromUnionDomainOf(RDFSClass cls, RDFProperty property, ClsEvent event) {
    	removedFromUnionDomainOf(cls, property);
    }
    
    public void removedFromUnionDomainOf(RDFSClass cls, RDFProperty property) {
        // Do nothing
    }
    
    public void instanceAdded(RDFSClass cls, RDFResource instance, ClsEvent event) {
    	instanceAdded(cls, instance);
    }


    public void instanceAdded(RDFSClass cls, RDFResource instance) {
        // Do nothing
    }

    public void instanceRemoved(RDFSClass cls, RDFResource instance, ClsEvent event) {
    	instanceRemoved(cls, instance);
    }


    public void instanceRemoved(RDFSClass cls, RDFResource instance) {
        // Do nothing
    }

    public void subclassAdded(RDFSClass cls, RDFSClass subclass, ClsEvent event) {
    	subclassAdded(cls, subclass);
    }
    
    public void subclassAdded(RDFSClass cls, RDFSClass subclass) {
        // Do nothing
    }

    public void subclassRemoved(RDFSClass cls, RDFSClass subclass, ClsEvent event) {
    	subclassRemoved(cls, subclass);
    }

    public void subclassRemoved(RDFSClass cls, RDFSClass subclass) {
        // Do nothing
    }

    public void superclassAdded(RDFSClass cls, RDFSClass superclass, ClsEvent event) {
    	superclassAdded(cls, superclass);
    }
    
    public void superclassAdded(RDFSClass cls, RDFSClass superclass) {
        // Do nothing
    }

    public void superclassRemoved(RDFSClass cls, RDFSClass superclass, ClsEvent event) {
    	superclassRemoved(cls, superclass);
    }

    public void superclassRemoved(RDFSClass cls, RDFSClass superclass) {
        // Do nothing
    }

    /************* Deprecated methods **************/

    /**
     * @deprecated
     */
    public final void directSubclassRemoved(ClsEvent event) {
        if (event.getCls() instanceof RDFSClass && event.getSubclass() instanceof RDFSClass) {
            subclassRemoved((RDFSClass) event.getCls(), (RDFSClass) event.getSubclass(), event);
        }
    }
    
    /**
     * @deprecated
     */
    public final void directSuperclassAdded(ClsEvent event) {
        if (event.getCls() instanceof RDFSClass && event.getSuperclass() instanceof RDFSClass) {
            superclassAdded((RDFSClass) event.getCls(), (RDFSClass) event.getSuperclass(), event);
        }
    }

    /**
     * @deprecated
     */
    public final void directSuperclassRemoved(ClsEvent event) {
        if (event.getCls() instanceof RDFSClass && event.getSuperclass() instanceof RDFSClass) {
            superclassRemoved((RDFSClass) event.getCls(), (RDFSClass) event.getSuperclass(), event);
        }
    }

    /**
     * @deprecated
     */
    public final void directInstanceAdded(ClsEvent event) {
        if (event.getCls() instanceof RDFSClass && event.getInstance() instanceof RDFResource) {
            instanceAdded((RDFSClass) event.getCls(), (RDFResource) event.getInstance(), event);
        }
    }

    /**
     * @deprecated
     */
    public final void directInstanceRemoved(ClsEvent event) {
        if (event.getCls() instanceof RDFSClass && event.getInstance() instanceof RDFResource) {
            instanceRemoved((RDFSClass) event.getCls(), (RDFResource) event.getInstance(), event);
        }
    }

    /**
     * @deprecated
     */
    public final void directSubclassAdded(ClsEvent event) {
        if (event.getCls() instanceof RDFSClass && event.getSubclass() instanceof RDFSClass) {
            subclassAdded((RDFSClass) event.getCls(), (RDFSClass) event.getSubclass(), event);
        }
    }
 
    /**
     * @deprecated
     */
    public final void templateSlotAdded(ClsEvent event) {
        if (event.getCls() instanceof RDFSClass && event.getSlot() instanceof RDFProperty) {
            addedToUnionDomainOf((RDFSClass) event.getCls(), (RDFProperty) event.getSlot(), event);
        }
    }

    /**
     * @deprecated
     */
    public final void templateSlotRemoved(ClsEvent event) {
        if (event.getCls() instanceof RDFSClass && event.getSlot() instanceof RDFProperty) {
            removedFromUnionDomainOf((RDFSClass) event.getCls(), (RDFProperty) event.getSlot(), event);
        }
    }

    /**
     * @deprecated
     */
    public final void templateSlotValueChanged(ClsEvent event) {
    }
    
    /**
     * @deprecated
     */
    public final void directSubclassMoved(ClsEvent event) {
    }
    
    /**
     * @deprecated
     */
    public final void templateFacetAdded(ClsEvent event) {
    }

    /**
     * @deprecated
     */
    public final void templateFacetRemoved(ClsEvent event) {
    }

    /**
     * @deprecated
     */
    public final void templateFacetValueChanged(ClsEvent event) {
    }

}
