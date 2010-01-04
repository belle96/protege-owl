package edu.stanford.smi.protegex.owl.ui.properties.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.properties.OWLSubpropertyPane;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateSubpropertyAction extends ResourceAction {

    public CreateSubpropertyAction() {
        super("Create subproperty", OWLIcons.getCreatePropertyIcon("SubProperty"));
    }


    public void actionPerformed(ActionEvent e) {
        OWLModel owlModel = getResource().getOWLModel();
        OWLSubpropertyPane pane = (OWLSubpropertyPane) getComponent();
        RDFProperty superProperty = (RDFProperty) getResource();
        performAction(owlModel, superProperty, pane);
    }


    public static void createInverseSlot(Slot slot, Collection superslots) {
    	//It is safe to assume that it is just one superslot.    	
    	Slot superSlot = (Slot) CollectionUtilities.getFirstItem(superslots);
    	Slot invSuperSlot = superSlot.getInverseSlot();
    	if (invSuperSlot == null) {
    		return;
    	}    	
    	Collection<Cls> metaClses = new ArrayList<Cls>(invSuperSlot.getDirectTypes());
    	String invName = slot.getName();
    	if (slot instanceof RDFResource) {
    		RDFResource resource = (RDFResource)slot;        		
			invName = resource.getNamespace() + "inverse_of_" + resource.getLocalName();
    	}
    	Cls firstMetaCls = CollectionUtilities.getFirstItem(metaClses);
        Slot inverse = slot.getKnowledgeBase().createSlot(invName, firstMetaCls, CollectionUtilities.createCollection(invSuperSlot), true);
        metaClses.remove(firstMetaCls);
        for (Iterator iterator = metaClses.iterator(); iterator.hasNext();) {
			Cls metacls = (Cls) iterator.next();
			inverse.addDirectType(metacls);
		}
        slot.setInverseSlot(inverse);
    }


    @Override
	public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof OWLSubpropertyPane && resource instanceof RDFProperty;
    }


    public static void performAction(OWLModel owlModel, RDFProperty superProperty, OWLSubpropertyPane pane) {
        RDFProperty subproperty = null;
        try {
        	String name = owlModel.createNewResourceName(superProperty.getLocalName());
            owlModel.beginTransaction("Create " + NamespaceUtil.getLocalName(name) + " as subproperty of " + NamespaceUtil.getLocalName(superProperty.getName()), name);            
            subproperty = owlModel.createSubproperty(name, superProperty);
            final Set superproperties = Collections.singleton(superProperty);
            createInverseSlot(subproperty, superproperties);
            if (superProperty instanceof OWLProperty &&
                    subproperty instanceof OWLProperty &&
                    ((OWLProperty) superProperty).isInverseFunctional()) {
                ((OWLProperty) subproperty).setInverseFunctional(true);
            }
            owlModel.commitTransaction();
        }
        catch (Exception ex) {
        	owlModel.rollbackTransaction();
            OWLUI.handleError(owlModel, ex);
        }
        pane.extendSelection(subproperty);
    }
}
