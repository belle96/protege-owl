package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddRDFTypeAction extends ResourceAction {

    public final static String GROUP = "rdf:types";


    public AddRDFTypeAction() {
        super("Add rdf:type...",
                OWLIcons.getAddIcon(OWLIcons.PRIMITIVE_OWL_CLASS), GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        RDFSClass type = ProtegeUI.getSelectionDialogFactory().selectClass(getComponent(), getOWLModel(), "Select an additional type...");
        if (type != null && !getResource().hasProtegeType(type)) {
            getResource().addProtegeType(type);
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource instanceof RDFIndividual && resource.isEditable();
    }
}
