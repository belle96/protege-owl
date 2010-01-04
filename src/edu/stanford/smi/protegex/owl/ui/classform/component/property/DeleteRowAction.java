package edu.stanford.smi.protegex.owl.ui.classform.component.property;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.ui.existential.Existential;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeleteRowAction extends AbstractAction {

    private PropertyFormTable table;


    public DeleteRowAction(PropertyFormTable table) {
        super("Delete selected rows", OWLIcons.getDeleteIcon());
        this.table = table;
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                updateEnabled();
            }
        });
        updateEnabled();
    }


    public void actionPerformed(ActionEvent e) {
        OWLObjectProperty prop = (OWLObjectProperty) table.getTableModel().getProperty();
        OWLNamedClass subject = table.getTableModel().getNamedClass();
        OWLNamedClass filler = (OWLNamedClass) table.getSelectedResource();
        OWLSomeValuesFrom restr =
                Existential.getDirectExistentialRelation(subject, prop, filler);
        subject.removeSuperclass(restr);
        //@@TODO update closure if required
    }


    // Public for testing purposes
    public boolean isEnabledFor(int[] selectedRows) {
        return selectedRows.length > 0;
    }


    private void updateEnabled() {
        int[] selectedRows = table.getSelectedRows();
        setEnabled(isEnabledFor(selectedRows));
    }
}
