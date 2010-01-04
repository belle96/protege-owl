package edu.stanford.smi.protegex.owl.ui.metadatatab.alldifferent;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.ui.widget.AbstractPropertyWidget;

import java.awt.*;

/**
 * A property widget to edit the AllDifferent's in a KnowledgeBase.
 * This is not actually assigned to any Slot, as the AllDifferentInstances are
 * independent, free-floating objects in the KnowledgeBase.
 * This widget is used in the OWLMetadataTab, where it can be assigned to the
 * Ontology-URI slot.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AllDifferentWidget extends AbstractPropertyWidget {

    public void initialize() {
        OWLModel owlModel = (OWLModel) getKnowledgeBase();
        add(BorderLayout.CENTER, new AllDifferentPanel(owlModel));
    }


    public static boolean isSuitable(Cls cls, edu.stanford.smi.protege.model.Slot slot, Facet facet) {
        return slot.getName().equals(OWLNames.Slot.BACKWARD_COMPATIBLE_WITH);
    }
}
