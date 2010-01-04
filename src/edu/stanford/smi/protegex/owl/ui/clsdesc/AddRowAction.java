package edu.stanford.smi.protegex.owl.ui.clsdesc;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.owltable.OWLTableModel;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * An Action that adds a class from a list into the table.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
class AddRowAction extends ResourceSelectionAction {

    private ClassDescriptionTable table;

    private boolean thingAllowed;


    AddRowAction(ClassDescriptionTable table, String name, boolean thingAllowed) {
        super(name, OWLIcons.getAddIcon(OWLIcons.PRIMITIVE_OWL_CLASS), true);
        this.table = table;
        this.thingAllowed = thingAllowed;
    }


    public void resourceSelected(RDFResource resource) {
        OWLModel owlModel = resource.getOWLModel();
        try {
            owlModel.beginTransaction(getValue(Action.NAME) + " " + resource.getBrowserText() +
                    " to " + table.getEditedCls().getBrowserText(), table.getEditedCls().getName());
            table.addCls((RDFSClass) resource);
            owlModel.commitTransaction();
        }
        catch (Exception ex) {
        	owlModel.rollbackTransaction();
            OWLUI.handleError(owlModel, ex);
        }
    }


    public Collection getSelectableResources() {
        final OWLModel owlModel = table.getOWLModel();
        Collection<OWLNamedClass> clses = owlModel.getUserDefinedOWLNamedClasses();
        if (thingAllowed) {
            clses.add(owlModel.getOWLThingClass());
        }
        Cls editedCls = ((OWLTableModel) table.getModel()).getEditedCls();
        clses.remove(editedCls); // Can never add itself
        OWLNamedClass[] cs = clses.toArray(new OWLNamedClass[0]);
        Arrays.sort(cs, new FrameComparator());
        return Arrays.asList(cs);
    }


    public Collection pickResources() {
        return ProtegeUI.getSelectionDialogFactory().selectClasses(table, table.getOWLModel(),
                "Select named class(es) to add");
    }
}
