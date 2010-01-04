package edu.stanford.smi.protegex.owl.ui.metadata;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.ui.widget.MultiResourceWidget;
import edu.stanford.smi.protegex.owl.ui.widget.MultiWidgetPropertyWidget;

/**
 * An AbstractSlotWidget that allows to toggle between a RestrictionsWidget and a
 * TemplateSlotsWidget using a JTabbedPane.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 * @deprecated 
 */
public class OWLMetadataWidget extends MultiWidgetPropertyWidget {

    private NameDocumentationWidget nameWidget;


    protected void createDifferentFromWidget() {
        MultiResourceWidget differentFromWidget = new MultiResourceWidget();
        addNestedWidget(differentFromWidget, OWLNames.Slot.DIFFERENT_FROM, "DifferentFrom", "owl:differentFrom");
    }


    /**
     * Can be overloaded by subclasses to add extra widgets after the labels,
     * or to suppress sameAs and differentFrom.
     */
    protected void createExtraWidgets() {
        createSameAsWidget();
        createDifferentFromWidget();
    }


    protected void createSameAsWidget() {
        MultiResourceWidget sameAsWidget = new MultiResourceWidget();
        addNestedWidget(sameAsWidget, OWLNames.Slot.SAME_AS, "SameAs", "owl:sameAs");
    }


    protected void createNestedWidgets() {

        nameWidget = new NameDocumentationWidget();
        addNestedWidget(nameWidget, Model.Slot.NAME, "Name", "Name");

        createExtraWidgets();
    }


    public NameDocumentationWidget getNameDocumentationWidget() {
        return nameWidget;
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return slot.getKnowledgeBase() instanceof OWLModel &&
                slot.getName().equals(Model.Slot.NAME);
    }
}
