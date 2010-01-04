package edu.stanford.smi.protegex.owl.ui.properties.actions;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConvertToObjectPropertyAction extends ResourceAction {

    public ConvertToObjectPropertyAction() {
        super("Convert to owl:ObjectProperty",
                OWLIcons.getImageIcon(OWLIcons.OWL_OBJECT_PROPERTY),
                ConvertToDatatypePropertyAction.GROUP,
                false);
    }


    public void actionPerformed(ActionEvent e) {
        RDFSClass type = getOWLModel().getOWLObjectPropertyClass();
        final RDFProperty property = (RDFProperty) getResource();
        try {
            getOWLModel().beginTransaction("Convert " + property.getBrowserText() + " to owl:DatatypeProperty", property.getName());
            property.setRange(null);
            property.setProtegeType(type);
            getOWLModel().commitTransaction();
        }
        catch (Exception ex) {
        	getOWLModel().rollbackTransaction();
            OWLUI.handleError(getOWLModel(), ex);
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource.isEditable() &&
                resource instanceof Slot &&
                ((Slot) resource).getDirectSuperslotCount() == 0 &&
                ((Slot) resource).getDirectSubslotCount() == 0 &&
                !(resource instanceof OWLObjectProperty);
    }
}
