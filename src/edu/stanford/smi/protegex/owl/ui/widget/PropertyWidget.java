package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.widget.SlotWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface PropertyWidget extends SlotWidget {


    RDFResource getEditedResource();


    /**
     * @see #getEditedResource
     * @deprecated
     */
    Instance getInstance();


    OWLModel getOWLModel();


    RDFProperty getRDFProperty();


    /**
     * @see #getRDFProperty
     * @deprecated
     */
    Slot getSlot();
}
