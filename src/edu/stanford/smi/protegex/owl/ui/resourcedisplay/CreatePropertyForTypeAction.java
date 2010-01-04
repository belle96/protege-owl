package edu.stanford.smi.protegex.owl.ui.resourcedisplay;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * An Action to add a new slot to the direct type of a given Instance.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreatePropertyForTypeAction extends AbstractAction {

    private RDFResource instance;


    public CreatePropertyForTypeAction(RDFResource instance) {
        super("Create new property for type...",
                OWLIcons.getImageIcon("CreatePropertyForType"));
        this.instance = instance;
    }


    public void actionPerformed(ActionEvent e) {
        RDFSClass directType = instance.getProtegeType();
        OWLModel owlModel = instance.getOWLModel();
        OWLProperty property = owlModel.createOWLDatatypeProperty(null);
        property.addUnionDomainClass(directType);
        directType.getProject().show(property);
    }
}
