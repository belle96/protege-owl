package edu.stanford.smi.protegex.owl.ui.menu.preferences;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protege.util.AllowableAction;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.util.SelectableList;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.framestore.OWLFrameStore;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SearchSettingsPanel extends JPanel {

    private JCheckBox ignorePrefixesCheckBox = new JCheckBox("Ignore prefixes for searching");

    private SelectableList list;

    private OWLModel owlModel;


    public SearchSettingsPanel(final OWLModel owlModel) {
        this.owlModel = owlModel;
        list = new SelectableList();
        list.setListData(owlModel.getSearchSynonymProperties().toArray());
        list.setCellRenderer(new ResourceRenderer());
        LabeledComponent lc = new OWLLabeledComponent("Use search synonyms", new JScrollPane(list));
        lc.addHeaderButton(new AbstractAction("Add property...",
                OWLIcons.getAddIcon(OWLIcons.RDF_PROPERTY)) {
            public void actionPerformed(ActionEvent e) {
                addProperty();
            }
        });
        lc.addHeaderButton(new AllowableAction("Remove selected property",
                OWLIcons.getRemoveIcon(OWLIcons.RDF_PROPERTY), list) {
            public void actionPerformed(ActionEvent e) {
                removeSelectedProperty();
            }
        });
        setLayout(new BorderLayout());

        Boolean b = owlModel.getOWLProject().getSettingsMap().getBoolean(OWLFrameStore.IGNORE_PREFIXES_IN_SEARCH);
        ignorePrefixesCheckBox.setSelected(Boolean.TRUE.equals(b));
        ignorePrefixesCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                owlModel.getOWLProject().getSettingsMap().setBoolean(OWLFrameStore.IGNORE_PREFIXES_IN_SEARCH,
                        ignorePrefixesCheckBox.isSelected());
            }
        });
        add(BorderLayout.NORTH, ignorePrefixesCheckBox);
        add(BorderLayout.CENTER, lc);
    }


    private void addProperty() {
        Collection oldValues = owlModel.getSearchSynonymProperties();
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
                properties, "Add search properties..."));
        owlModel.setSearchSynonymProperties(neo);
        list.setListData(owlModel.getSearchSynonymProperties().toArray());
    }


    private void removeSelectedProperty() {
        Collection oldValues = owlModel.getSearchSynonymProperties();
        oldValues.removeAll(list.getSelection());
        owlModel.setSearchSynonymProperties(oldValues);
        list.setListData(owlModel.getSearchSynonymProperties().toArray());
    }
}
