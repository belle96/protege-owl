package edu.stanford.smi.protegex.owl.ui.components.triples;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeleteTripleAction extends AbstractAction {

    private TriplesTable table;


    public DeleteTripleAction(TriplesTable table) {
        this(table, "Delete selected value", OWLIcons.getDeleteIcon());
    }


    public DeleteTripleAction(TriplesTable table, String name, Icon icon) {
        super(name, icon);
        this.table = table;
    };


    public void actionPerformed(ActionEvent e) {    	
    	if (table.getCellEditor() != null)
			table.getCellEditor().stopCellEditing();
    	
        table.removeEditor();
        int row = table.getSelectedRow();
        if (row >= 0) {
        	table.getTableModel().deleteRow(row);
        }
    }
}
