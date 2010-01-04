package edu.stanford.smi.protegex.owl.ui.properties.actions;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConvertToDatatypePropertyAction extends ResourceAction {

    public static final String GROUP = "ConvertType";


    public ConvertToDatatypePropertyAction() {
        super("Convert to owl:DatatypeProperty",
                OWLIcons.getImageIcon(DefaultOWLDatatypeProperty.ICON_NAME),
                GROUP, false);
    }


    public void actionPerformed(ActionEvent e) {
        RDFSClass type = getOWLModel().getOWLDatatypePropertyClass();
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
                !(resource instanceof OWLDatatypeProperty);
    }
}
