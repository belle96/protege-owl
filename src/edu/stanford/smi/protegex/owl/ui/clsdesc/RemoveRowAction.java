package edu.stanford.smi.protegex.owl.ui.clsdesc;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.cls.OWLClassesTab;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.owltable.AbstractOWLTableAction;
import edu.stanford.smi.protegex.owl.ui.owltable.OWLTableModel;

import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * A OWLTableAction that removes the selected row from the table.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
class RemoveRowAction extends AbstractOWLTableAction {

    private ClassDescriptionTable table;


    RemoveRowAction(ClassDescriptionTable table) {
        super("Remove selected class from list", OWLIcons.getRemoveIcon(OWLIcons.PRIMITIVE_OWL_CLASS));
        this.table = table;
    }


    public void actionPerformed(ActionEvent e) {
        OWLClassesTab tab = OWLClassesTab.getOWLClassesTab(table);
        int[] sels = table.getSelectedRows();
        Arrays.sort(sels);
        final OWLNamedClass editedClass = table.getEditedCls();
        for (int i = sels.length - 1; i >= 0; i--) {
            int selIndex = sels[i];
            OWLTableModel tableModel = (OWLTableModel) table.getModel();
            Cls cls = tableModel.getClass(selIndex);
            if (tableModel.isCellEditable(selIndex, tableModel.getSymbolColumnIndex()) ||
                    cls.equals(cls.getKnowledgeBase().getRootCls())) {
                tableModel.deleteRow(selIndex);
                table.getSelectionModel().setSelectionInterval(selIndex, selIndex);
            }
        }
        if (tab != null) {
            tab.ensureClsSelected(editedClass, -1);
        }
    }


    public boolean isEnabledFor(RDFSClass cls, int rowIndex) {
        ClassDescriptionTableModel tableModel = (ClassDescriptionTableModel) table.getModel();
        return cls != null && tableModel.isRemoveEnabledFor(cls);
    }
}
