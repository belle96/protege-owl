package edu.stanford.smi.protegex.owl.ui.conditions;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.cls.OWLClassesTab;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.owltable.AbstractOWLTableAction;
import edu.stanford.smi.protegex.owl.ui.owltable.OWLTableAction;
import edu.stanford.smi.protegex.owl.ui.widget.AbstractPropertyWidget;
import edu.stanford.smi.protegex.owl.ui.widget.WidgetUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A SlotWidget used for describing logical class characteristics, i.e.
 * superclasses and equivalent classes.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractConditionsWidget extends AbstractPropertyWidget
        implements ConditionsTableConstants, PropertyConditionsDisplay {


    protected ConditionsTable table;

    protected ConditionsTableModel tableModel;

    /*private OWLTableAction navigateToClsAction =
            new AbstractOWLTableAction("Navigate to class in hierarchy", Icons.getBlankIcon()) {
                public void actionPerformed(ActionEvent e) {
                    Cls aClassassass = table.getSelectedCls();
                    if (aClassassass instanceof RDFSNamedClass) {
                        navigateToCls((RDFSNamedClass) aClassassass);
                    }
                }


                public boolean isEnabledFor(Cls superCls, int rowIndex) {
                    return superCls instanceof RDFSNamedClass;
                }
            };*/

    private OWLTableAction viewAction =
            new AbstractOWLTableAction("View/edit class", OWLIcons.getViewIcon()) {
                public void actionPerformed(ActionEvent e) {
                    Cls cls = table.getSelectedCls();
                    if (cls != null) {
                        showInstance(cls);
                    }
                }


                public boolean isEnabledFor(RDFSClass cls, int rowIndex) {
                    return cls instanceof RDFSNamedClass;
                }
            };


    public void displayRowsWithProperty(OWLProperty property) {
        table.displayRowsWithProperty(property);
    }


    protected OWLClassesTab getClsesTab() {
        return OWLClassesTab.getOWLClassesTab(this);
    }


    protected OWLNamedClass getEditedCls() {
        return (OWLNamedClass) getEditedResource();
    }


    public ConditionsTable getTable() {
        return table;
    }


    protected void initialize(String label, Slot superclassesSlot) {
        OWLModel owlModel = (OWLModel) getKnowledgeBase();
        tableModel = new ConditionsTableModel(superclassesSlot);
        table = new ConditionsTable(owlModel, tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JViewport viewPort = scrollPane.getViewport();
        viewPort.setBackground(table.getBackground());
        LabeledComponent labeledComponent = new OWLLabeledComponent(label, scrollPane, true, true);
        //labeledComponent.setHeaderIcon(headerIcon);
        initializeButtons(labeledComponent);
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, labeledComponent);
    }


    protected void initializeButtons(LabeledComponent labeledComponent) {
        WidgetUtilities.addViewButton(labeledComponent, viewAction);
        // table.registerAction(viewAction);
        // table.registerAction(navigateToClsAction, 3);
    }


    private void navigateToCls(RDFSNamedClass rdfsClass) {
        OWLClassesTab tab = getClsesTab();
        if (tab != null) {
            tab.setSelectedCls(rdfsClass);
        }
    }


    public void refresh() {
        tableModel.refresh();
    }


    public void setInstance(Instance newInstance) {
        super.setInstance(newInstance);
        if (table != null) {
            if (newInstance instanceof OWLNamedClass) {
                table.setCls((OWLNamedClass) newInstance);
                tableModel.setCls((OWLNamedClass) newInstance);
            }
            else {
                tableModel.setCls(null);
            }
        }
    }
}
