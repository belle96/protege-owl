package edu.stanford.smi.protegex.owl.ui.components.annotations;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.components.triples.TriplesTable;
import edu.stanford.smi.protegex.owl.ui.components.triples.TriplesTableModel;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddTodoAction extends AbstractAction {

    private TriplesTable table;


    public AddTodoAction(TriplesTable table) {
        super("Add TODO list item", OWLIcons.getCreateIcon("TodoAnnotation"));
        this.table = table;
    }


    public void actionPerformed(ActionEvent e) {
        TriplesTableModel tableModel = table.getTableModel();
        OWLModel owlModel = tableModel.getOWLModel();
        OWLDatatypeProperty todoProperty = owlModel.getTodoAnnotationProperty();
        String prefix = owlModel.getTodoAnnotationPrefix();
        String value = prefix + ": ";
        Collection existingValues = tableModel.getSubject().getPropertyValues(todoProperty);
        while (existingValues.contains(value)) {
            value += "-";
        }
        tableModel.getSubject().addPropertyValue(todoProperty, value);
        int row = tableModel.getPropertyValueRow(todoProperty, value);
        table.getSelectionModel().setSelectionInterval(row, row);
        table.editCell(row);
    }
}
