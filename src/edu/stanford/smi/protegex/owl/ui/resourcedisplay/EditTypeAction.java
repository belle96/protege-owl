package edu.stanford.smi.protegex.owl.ui.resourcedisplay;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * An Action that shows the direct type of a given Instance.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class EditTypeAction extends AbstractAction {

    private RDFResource resource;


    public EditTypeAction(RDFResource resource) {
        super("Edit type...", OWLIcons.getImageIcon("EditType"));
        this.resource = resource;
    }


    public void actionPerformed(ActionEvent e) {
        if (EditTypeFormAction.ensureMetaClsVisible(resource)) {
            RDFSClass directType = resource.getProtegeType();
            resource.getProject().show(directType);
        }
    }
}
