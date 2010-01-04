package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RemoveRDFTypeAction extends ResourceAction {

    public RemoveRDFTypeAction() {
        super("Remove rdf:type...",
                OWLIcons.getRemoveIcon(OWLIcons.PRIMITIVE_OWL_CLASS),
                AddRDFTypeAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        RDFSClass type = (RDFSClass) ProtegeUI.getSelectionDialogFactory().selectResourceFromCollection(getComponent(),
                getOWLModel(),
                getResource().getProtegeTypes(),
                "Select an rdf:type to remove...");
        if (type != null) {
            getResource().removeProtegeType(type);
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource.getProtegeTypes().size() > 1 &&
                resource.isEditable();
    }
}
