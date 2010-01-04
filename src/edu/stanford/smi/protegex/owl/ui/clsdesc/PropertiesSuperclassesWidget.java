package edu.stanford.smi.protegex.owl.ui.clsdesc;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.event.ClassAdapter;
import edu.stanford.smi.protegex.owl.model.event.ClassListener;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.owltable.OWLTableModel;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * A ClassDescriptionWidget that toggles between a PropertiesSuperclassesTableModel
 * and a PropertiesDefinitionTableModel depending on whether the current class is
 * defined or not.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertiesSuperclassesWidget extends ClassDescriptionWidget {

    private ResourceSelectionAction addRowAction;

    private ClassListener classListener = new ClassAdapter() {
        public void subclassAdded(RDFSClass cls, RDFSClass subclass) {
            updateModel();
        }


        public void subclassRemoved(RDFSClass cls, RDFSClass subclass) {
            updateModel();
        }


        public void superclassAdded(RDFSClass cls, RDFSClass superclass) {
            updateModel();
        }


        public void superclassRemoved(RDFSClass cls, RDFSClass superclass) {
            updateModel();
        }
    };


    private Action createRowAction;


    protected ResourceSelectionAction createAddAction(ClassDescriptionTable table) {
        addRowAction = new AddRowAction(getTable(), "Add named class", true);
        return addRowAction;
    }


    protected Action createCreateAction(ClassDescriptionTable table) {
        createRowAction = new CreateRowAction(getTable(), "Create class from expression");
        return createRowAction;
    }


    protected List createCustomActions(ClassDescriptionTable table) {
        return Collections.EMPTY_LIST;
    }


    protected Icon createHeaderIcon() {
        return OWLIcons.getSuperclassesIcon();
    }


    protected OWLTableModel createTableModel() {
        return new PropertiesSuperclassesTableModel();
    }


    public void dispose() {
        super.dispose();
        if (getEditedCls() != null) {
            getEditedCls().removeClassListener(classListener);
        }
    }


    protected String getLabelText() {
        return "Superclasses";
    }


    public void setInstance(Instance newInstance) {
        if (getEditedCls() != null) {
            getEditedCls().removeClassListener(classListener);
        }
        super.setInstance(newInstance);
        updateModel();
        if (getEditedCls() != null) {
            getEditedCls().addClassListener(classListener);
            boolean thing = getKnowledgeBase().getRootCls().equals(getEditedCls());
            addRowAction.setEnabled(!thing && getEditedCls().isEditable());
            createRowAction.setEnabled(!thing && getEditedCls().isEditable());
        }
    }


    private void updateModel() {
        OWLNamedClass cls = getEditedCls();
        if (cls != null) {
            Cls definition = cls.getDefinition();
            if (definition != null) {
                if (!(getTable().getModel() instanceof PropertiesDefinitionTableModel)) {
                    PropertiesDefinitionTableModel tableModel = new PropertiesDefinitionTableModel();
                    tableModel.setCls(cls);
                    getTable().setModel(tableModel);
                }
                getLabeledComponent().setHeaderLabel("Defining Classes");
                getLabeledComponent().setHeaderIcon(OWLIcons.getEquivalentClassIcon());
            }
            else {
                if (!(getTable().getModel() instanceof PropertiesSuperclassesTableModel)) {
                    PropertiesSuperclassesTableModel tableModel = new PropertiesSuperclassesTableModel();
                    tableModel.setCls(cls);
                    getTable().setModel(tableModel);
                }
                getLabeledComponent().setHeaderLabel("Superclasses");
                getLabeledComponent().setHeaderIcon(OWLIcons.getSuperclassesIcon());
            }
        }
    }
}
