package edu.stanford.smi.protegex.owl.ui.conditions;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.cls.OWLClassesTab;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.owltable.OWLTableModel;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;

import java.util.Arrays;
import java.util.Collection;

/**
 * An Action to add a named class as a superclass into the conditions table.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
class AddNamedClassAction extends ResourceSelectionAction
        implements ConditionsTableConstants {

    private ConditionsTable table;


    AddNamedClassAction(ConditionsTable table) {
        super("Add named class...", OWLIcons.getAddIcon("PrimitiveClass"));
        this.table = table;
    }


    public void resourceSelected(RDFResource resource) {
        final Cls editedCls = table.getEditedCls();
        table.selectNecessaryIfNothingSelected();
        OWLClassesTab tab = OWLClassesTab.getOWLClassesTab(table);
        int row = table.addRow((Cls) resource);
        if (tab != null) {
            tab.ensureClsSelected(editedCls, row);
        }
    }


    public Collection getSelectableResources() {
        Collection<OWLNamedClass> clses = table.getOWLModel().getUserDefinedOWLNamedClasses();
        clses.add(table.getOWLModel().getOWLThingClass());
        clses.remove(((OWLTableModel) table.getModel()).getEditedCls());
        ConditionsTableModel tableModel = (ConditionsTableModel) table.getModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Cls cls = tableModel.getClass(i);
            if (cls instanceof OWLNamedClass) {
                clses.remove(cls);
            }
        }
        return clses;
    }


    public RDFResource pickResource() {
        return (RDFResource) ProtegeUI.getSelectionDialogFactory().selectClass(table, table.getOWLModel(),
                "Select a named class to add");
    }
}
