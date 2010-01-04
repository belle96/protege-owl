package edu.stanford.smi.protegex.owl.ui.owltable;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.cls.OWLClassesTab;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import java.awt.event.ActionEvent;

/**
 * A OWLTableAction that deletes the selected row from the table.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeleteRowAction extends AbstractOWLTableAction {

    private OWLTable table;


    public DeleteRowAction(OWLTable table) {
        super("Delete selected row",
                OWLIcons.getDeleteIcon(OWLIcons.ANONYMOUS_OWL_CLASS));
        this.table = table;
    }


    public void actionPerformed(ActionEvent e) {
        OWLClassesTab tab = OWLClassesTab.getOWLClassesTab(table);
        final OWLNamedClass editedClass = table.getEditedCls();
        int selIndex = table.getSelectedRow();
        if (selIndex >= 0) {
            OWLTableModel tableModel = (OWLTableModel) table.getModel();
            if (tableModel.isCellEditable(selIndex, tableModel.getSymbolColumnIndex())) {
                tableModel.deleteRow(selIndex);
                table.getSelectionModel().setSelectionInterval(selIndex, selIndex);
                if (tab != null) {
                    tab.ensureClassSelected(editedClass, -1);
                }
            }
        }
    }


    public boolean isEnabledFor(RDFSClass cls, int rowIndex) {
        OWLTableModel tableModel = (OWLTableModel) table.getModel();
        return cls != null &&
                tableModel.isDeleteEnabledFor(cls);
    }
}
