package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.widget.AbstractSlotWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractPropertyWidget extends AbstractSlotWidget
        implements PropertyWidget {

    public OWLModel getOWLModel() {
        return (OWLModel) getKnowledgeBase();
    }


    /**
     * @deprecated
     */
    public Instance getInstance() {
        return super.getInstance();
    }


    public RDFProperty getRDFProperty() {
        return (RDFProperty) super.getSlot();
    }


    public RDFResource getEditedResource() {
        return (RDFResource) super.getInstance();
    }


    /**
     * @deprecated
     */
    public Slot getSlot() {
        return super.getSlot();
    }


    protected void updateBorder(Collection values) {
        // Don't do anything to avoid facet testing
    }
}
