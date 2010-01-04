package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protege.event.SlotEvent;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyAdapter implements PropertyListener {

    public void subpropertyAdded(RDFProperty property, RDFProperty subproperty, SlotEvent event) {
    	subpropertyAdded(property, subproperty);
    }

    public void subpropertyAdded(RDFProperty property, RDFProperty subproperty) {
        // Do nothing
    }

    public void subpropertyRemoved(RDFProperty property, RDFProperty subproperty, SlotEvent event) {
    	subpropertyRemoved(property, subproperty);
    }

    public void subpropertyRemoved(RDFProperty property, RDFProperty subproperty) {
        // Do nothing
    }

    public void superpropertyAdded(RDFProperty property, RDFProperty superproperty, SlotEvent event) {
    	superpropertyAdded(property, superproperty);
    }
 

    public void superpropertyAdded(RDFProperty property, RDFProperty superproperty) {
        // Do nothing
    }

    public void superpropertyRemoved(RDFProperty property, RDFProperty superproperty, SlotEvent event) {
    	superpropertyRemoved(property, superproperty);
    }

    public void superpropertyRemoved(RDFProperty property, RDFProperty superproperty) {
        // Do nothing
    }

    public void unionDomainClassAdded(RDFProperty property, RDFSClass rdfsClass, SlotEvent event) {
    	unionDomainClassAdded(property, rdfsClass);
    }

    public void unionDomainClassAdded(RDFProperty property, RDFSClass rdfsClass) {
        // Do nothing
    }

    public void unionDomainClassRemoved(RDFProperty property, RDFSClass rdfsClass, SlotEvent event) {
    	unionDomainClassRemoved(property, rdfsClass);
    }
        
    public void unionDomainClassRemoved(RDFProperty property, RDFSClass rdfsClass) {
        // Do nothing
    }
        

    /************* Deprecated methods **************/
    
    /**
     * @deprecated
     */
    public final void templateSlotClsAdded(SlotEvent event) {
        if (event.getSlot() instanceof RDFProperty && event.getCls() instanceof RDFSClass) {
            unionDomainClassAdded((RDFProperty) event.getSlot(), (RDFSClass) event.getCls(), event);
        }
    }


    /**
     * @deprecated
     */
    public void templateSlotClsRemoved(SlotEvent event) {
        if (event.getSlot() instanceof RDFProperty && event.getCls() instanceof RDFSClass) {
            unionDomainClassRemoved((RDFProperty) event.getSlot(), (RDFSClass) event.getCls(), event);
        }
    }
    
    /**
     * @deprecated
     */
    public void directSubslotAdded(SlotEvent event) {
        if (event.getSlot() instanceof RDFProperty && event.getSubslot() instanceof RDFProperty) {
            subpropertyAdded((RDFProperty) event.getSlot(), (RDFProperty) event.getSubslot(), event);
        }
    }


    /**
     * @deprecated
     */
    public final void directSubslotMoved(SlotEvent event) {
        // Not supported in OWL
    }
    
    /**
     * @deprecated
     */
    public void directSubslotRemoved(SlotEvent event) {
        if (event.getSlot() instanceof RDFProperty && event.getSubslot() instanceof RDFProperty) {
            subpropertyRemoved((RDFProperty) event.getSlot(), (RDFProperty) event.getSubslot(), event);
        }
    }


    /**
     * @deprecated
     */
    public void directSuperslotAdded(SlotEvent event) {
        if (event.getSlot() instanceof RDFProperty && event.getSubslot() instanceof RDFProperty) {
            superpropertyAdded((RDFProperty) event.getSlot(), (RDFProperty) event.getSubslot(), event);
        }
    }


    /**
     * @deprecated
     */
    public void directSuperslotRemoved(SlotEvent event) {
        if (event.getSlot() instanceof RDFProperty && event.getSubslot() instanceof RDFProperty) {
            superpropertyRemoved((RDFProperty) event.getSlot(), (RDFProperty) event.getSubslot(), event);
        }
    }
    
}
