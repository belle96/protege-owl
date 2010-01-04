package edu.stanford.smi.protegex.owl.ui.clsdesc;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.owltable.AbstractOWLTableAction;
import edu.stanford.smi.protegex.owl.ui.owltable.DeleteRowAction;
import edu.stanford.smi.protegex.owl.ui.owltable.OWLTableAction;
import edu.stanford.smi.protegex.owl.ui.owltable.OWLTableModel;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;
import edu.stanford.smi.protegex.owl.ui.widget.AbstractPropertyWidget;
import edu.stanford.smi.protegex.owl.ui.widget.WidgetUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;

/**
 * An AbstractSlotWidget that displays the superclasses / equivalent classes in a table.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class ClassDescriptionWidget extends AbstractPropertyWidget {

    private ClassDescriptionTable table;

    private OWLTableAction viewAction = new AbstractOWLTableAction("View/edit class", Icons.getViewIcon()) {
        public void actionPerformed(ActionEvent e) {
            Cls cls = table.getSelectedCls();
            if (cls != null) {
                showInstance(cls);
            }
        }


        public boolean isEnabledFor(RDFSClass cls, int rowIndex) {
            return cls instanceof OWLNamedClass;
        }
    };


    protected abstract ResourceSelectionAction createAddAction(final ClassDescriptionTable table);


    protected abstract Action createCreateAction(final ClassDescriptionTable table);


    protected abstract Icon createHeaderIcon();


    protected abstract java.util.List createCustomActions(final ClassDescriptionTable table);


    protected abstract OWLTableModel createTableModel();


    public void dispose() {
        ((OWLTableModel) table.getModel()).dispose();
        super.dispose();
    }


    public OWLNamedClass getEditedCls() {
        return table.getEditedCls(); // (OWLNamedClass) getInstance();
    }


    protected OWLLabeledComponent getLabeledComponent() {
        return (OWLLabeledComponent) getComponent(0);
    }


    protected abstract String getLabelText();


    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }


    public ClassDescriptionTable getTable() {
        return table;
    }

    public void initialize() {
        OWLTableModel tableModel = createTableModel();
        OWLModel owlModel = (OWLModel) getProject().getKnowledgeBase();
        table = new ClassDescriptionTable(owlModel, tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JViewport viewPort = scrollPane.getViewport();
        viewPort.setBackground(table.getBackground());
        LabeledComponent labeledComponent = new OWLLabeledComponent(getLabelText(), scrollPane, true, true);
        Icon headerIcon = createHeaderIcon();
        if (headerIcon != null) {
            labeledComponent.setHeaderIcon(headerIcon);
        }
        WidgetUtilities.addViewButton(labeledComponent, viewAction);
        labeledComponent.addHeaderButton(createCreateAction(table));
        ResourceSelectionAction addAction = createAddAction(table);
        labeledComponent.addHeaderButton(addAction);
        table.registerAction(viewAction);
        for (Iterator it = createCustomActions(table).iterator(); it.hasNext();) {
            Action action = (Action) it.next();
            labeledComponent.addHeaderButton(action);
            if (action instanceof OWLTableAction) {
                table.registerAction((OWLTableAction) action);
            }
        }
        OWLTableAction deleteAction = new DeleteRowAction(table);
        //OWLTableAction removeAction = new RemoveRowAction(table);
        //labeledComponent.addHeaderButton(removeAction);
        labeledComponent.addHeaderButton(deleteAction);
        //table.registerAction(removeAction);
        table.registerAction(deleteAction);
        table.registerActionSeparator();
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, labeledComponent);
    }


    public void setInstance(Instance newInstance) {
        super.setInstance(newInstance);
        if (newInstance instanceof OWLNamedClass) {
            table.setCls((OWLNamedClass) newInstance);
        }
        else {
            table.setCls(null);
        }
    }
}
