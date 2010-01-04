package edu.stanford.smi.protegex.owl.ui.metadata;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.widget.ClsWidget;
import edu.stanford.smi.protege.widget.SlotWidget;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.components.PropertyValuesComponent;
import edu.stanford.smi.protegex.owl.ui.components.annotations.AnnotationsComponent;
import edu.stanford.smi.protegex.owl.ui.components.triples.TriplesTableModel;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.ui.widget.AbstractPropertyValuesWidget;
import edu.stanford.smi.protegex.owl.ui.widget.OWLWidgetMapper;

import java.awt.*;

/**
 * A PropertyWidget to edit the values of annotation properties.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AnnotationsWidget extends AbstractPropertyValuesWidget {

    protected PropertyValuesComponent createComponent(RDFProperty predicate) {
        return new AnnotationsComponent(predicate, isReadOnlyConfiguredWidget()) {
            protected void handleSelectionChanged() {
                super.handleSelectionChanged();
                updateTextAreaWidget();
            }


            /**
             * Attempts to locate an associated AnnotationTextAreaWidget and then notifies it
             * about the current selection.
             */
            private void updateTextAreaWidget() {
                ClsWidget clsWidget = getClsWidget();
                if (clsWidget != null) {
                    OWLModel owlModel = getOWLModel();
                    TriplesTableModel tableModel = getTableModel();
                    Slot nameSlot = ((KnowledgeBase) owlModel).getSlot(Model.Slot.NAME);
                    SlotWidget slotWidget = clsWidget.getSlotWidget(nameSlot);
                    if (slotWidget instanceof OWLMetadataWidget) {
                        NameDocumentationWidget ndw = ((OWLMetadataWidget) slotWidget).getNameDocumentationWidget();
                        AnnotationTextAreaWidget ataw = ndw.getAnnotationTextAreaWidget();
                        int row = getTable().getSelectedRow();
                        if (row >= 0) {
                            RDFProperty selectedProperty = tableModel.getPredicate(row);
                            if (selectedProperty instanceof OWLDatatypeProperty) {
                                RDFResource range = selectedProperty.getRange();
                                if (range == null ||
                                        owlModel.getXSDstring().equals(range) ||
                                        owlModel.getRDFXMLLiteralType().equals(range)) {
                                    OWLDatatypeProperty property = (OWLDatatypeProperty) selectedProperty;
                                    String language = tableModel.getLanguage(row);
                                    final Object v = tableModel.getDisplayValue(row);
                                    if (v instanceof String) {
                                        String value = (String) v;
                                        ataw.setEditedValue(property, value, language, tableModel.getValue(row));
                                    }
                                }
                            }
                        }
                        else {
                            ataw.resetEditedValue();
                        }
                    }
                }
            }
        };
    }


    public static void editAnnotations(Component parent, RDFResource instance) {
        AnnotationsWidget widget = new AnnotationsWidget();
        widget.setup(null, false, instance.getProject());
        widget.initialize();
        widget.setCls(instance.getOWLModel().getOWLThingClass());  // Any
        widget.setInstance(instance);
        ProtegeUI.getModalDialogFactory().showDialog(parent, widget, "Annotations", ModalDialogFactory.MODE_CLOSE);
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return OWLWidgetMapper.isSuitable(AnnotationsWidget.class, cls, slot);
    }
}
