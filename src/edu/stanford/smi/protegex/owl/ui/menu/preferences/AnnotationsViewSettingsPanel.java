package edu.stanford.smi.protegex.owl.ui.menu.preferences;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protege.util.AllowableAction;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.util.SelectableList;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLModel;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

public class AnnotationsViewSettingsPanel extends JPanel {

    private SelectableList list;

    private OWLModel owlModel;


    public AnnotationsViewSettingsPanel(final OWLModel owlModel) {
        this.owlModel = owlModel;
        list = new SelectableList();
        list.setListData(((AbstractOWLModel)owlModel).getDefaultAnnotationPropertiesInView().toArray());
        list.setCellRenderer(new ResourceRenderer());
        LabeledComponent lc = new OWLLabeledComponent("Default annotation properties to be shown in the Annotation Widget", new JScrollPane(list));
        lc.addHeaderButton(new AbstractAction("Add property...", OWLIcons.getAddIcon(OWLIcons.RDF_PROPERTY)) {
            public void actionPerformed(ActionEvent e) {
                addProperty();
            }
        });
        
        lc.addHeaderButton(new AllowableAction("Remove selected property", OWLIcons.getRemoveIcon(OWLIcons.RDF_PROPERTY), list) {
            public void actionPerformed(ActionEvent e) {
                removeSelectedProperty();
            }
        });
        
        setLayout(new BorderLayout());        
        add(BorderLayout.CENTER, lc);
    }


    private void addProperty() {
        Collection oldValues = ((AbstractOWLModel)owlModel).getDefaultAnnotationPropertiesInView();
        Collection properties = owlModel.getRDFProperties();
        for (Iterator it = properties.iterator(); it.hasNext();) {
            RDFProperty property = (RDFProperty) it.next();
            if (oldValues.contains(property) ||
                    ( ((Slot) property).getValueType() != ValueType.STRING && !property.isAnnotationProperty())   ){
                it.remove();
            }
        }
        
        Collection neo = new HashSet(oldValues);
        neo.addAll(ProtegeUI.getSelectionDialogFactory().selectResourcesFromCollection(this, owlModel,
                properties, "Add properties..."));
        ((AbstractOWLModel)owlModel).setDefaultAnnotationPropertiesInView(neo);
        list.setListData(((AbstractOWLModel)owlModel).getDefaultAnnotationPropertiesInView().toArray());
    }


    private void removeSelectedProperty() {
        Collection oldValues = ((AbstractOWLModel)owlModel).getDefaultAnnotationPropertiesInView();
        oldValues.removeAll(list.getSelection());
        ((AbstractOWLModel)owlModel).setDefaultAnnotationPropertiesInView(oldValues);
        list.setListData(((AbstractOWLModel)owlModel).getDefaultAnnotationPropertiesInView().toArray());
    }
}
