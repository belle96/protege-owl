package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeletePropertyAction extends ResourceAction {

    public DeletePropertyAction() {
        super("Delete property", OWLIcons.getDeleteIcon(OWLIcons.RDF_PROPERTY));
    }


    public void actionPerformed(ActionEvent e) {
        if ((e.getModifiers() & ActionEvent.CTRL_MASK) != 0 ||
                ProtegeUI.getModalDialogFactory().showConfirmDialog(getComponent(),
                        "Do you really want to delete " + getResource().getBrowserText() + "?",
                        "Confirm Delete")) {
            getResource().delete();
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource instanceof RDFProperty && resource.isEditable() && component instanceof JTree;
    }
}
