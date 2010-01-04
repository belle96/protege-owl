package edu.stanford.smi.protegex.owl.ui.clsdesc;

import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * An Action that adds a class expression into the table.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
class CreateRowAction extends AbstractAction {

    private ClassDescriptionTable table;


    CreateRowAction(ClassDescriptionTable table, String name) {
        super(name, OWLIcons.getCreateIcon(OWLIcons.ANONYMOUS_OWL_CLASS));
        this.table = table;
    }


    public void actionPerformed(ActionEvent e) {
        table.createAndEditRow();
    }
}
