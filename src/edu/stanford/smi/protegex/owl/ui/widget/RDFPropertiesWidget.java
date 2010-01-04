package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.widget.AbstractSlotWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.clsproperties.RDFPropertiesComponent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFPropertiesWidget extends AbstractSlotWidget {

    private RDFPropertiesComponent component;


    public RDFPropertiesWidget() {
        setPreferredColumns(2);
        setPreferredRows(2);
    }


    public void initialize() {
        OWLModel owlModel = (OWLModel) getKnowledgeBase();
        component = new RDFPropertiesComponent(owlModel);
        add(component);
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return cls.getKnowledgeBase() instanceof OWLModel &&
                Model.Slot.DIRECT_TEMPLATE_SLOTS.equals(slot.getName());
    }


    public void setInstance(Instance newInstance) {
        super.setInstance(newInstance);
        if (newInstance instanceof RDFSNamedClass) {
            component.setClass((RDFSNamedClass) newInstance);
        }
    }
}
