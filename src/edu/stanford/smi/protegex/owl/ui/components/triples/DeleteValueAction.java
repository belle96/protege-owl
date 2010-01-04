package edu.stanford.smi.protegex.owl.ui.components.triples;

import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @deprecated should be deleted
 */
public class DeleteValueAction extends AbstractAction {

    private TriplesTable table;


    public DeleteValueAction(TriplesTable table) {
        super("Delete selected annotation", OWLIcons.getDeleteIcon("Annotation"));
        this.table = table;
    }


    ;


    public void actionPerformed(ActionEvent e) {
        table.removeEditor();
        int row = table.getSelectedRow();
        table.getTableModel().deleteRow(row);
    }
}
