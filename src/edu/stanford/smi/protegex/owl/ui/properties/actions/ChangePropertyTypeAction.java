package edu.stanford.smi.protegex.owl.ui.properties.actions;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.DisplayUtilities;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.profiles.OWLProfiles;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfilesManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ChangePropertyTypeAction extends ResourceAction {

    public ChangePropertyTypeAction() {
        super("Change property metaclass...",
                Icons.getBlankIcon(),
                ConvertToDatatypePropertyAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        RDFProperty property = (RDFProperty) getResource();
        Slot slot = property;
        Cls rootType = getRootType(slot);

        RDFSNamedClass rdfPropertyClass = getOWLModel().getRDFPropertyClass();
        boolean rdfPropertyWasVisible = rdfPropertyClass.isVisible();
        rdfPropertyClass.setVisible(true);

        RDFSNamedClass owlObjectPropertyClass = getOWLModel().getOWLObjectPropertyClass();
        boolean owlObjectPropertyWasVisible = owlObjectPropertyClass.isVisible();
        boolean objectPropertiesAllowed = ProfilesManager.isFeatureSupported(getOWLModel(), OWLProfiles.Create_ObjectProperty);
        owlObjectPropertyClass.setVisible(objectPropertiesAllowed);

        RDFSNamedClass owlDatatypePropertyClass = getOWLModel().getOWLDatatypePropertyClass();
        boolean owlDatatypePropertyWasVisible = owlDatatypePropertyClass.isVisible();
        boolean datatypePropertiesAllowed = ProfilesManager.isFeatureSupported(getOWLModel(), OWLProfiles.Create_DatatypeProperty);
        owlDatatypePropertyClass.setVisible(datatypePropertiesAllowed);

        RDFSClass type = (RDFSClass) ProtegeUI.getSelectionDialogFactory().selectClass(getComponent(), getOWLModel(),
                Collections.singleton(rootType),
                "Select property metaclass");

        rdfPropertyClass.setVisible(rdfPropertyWasVisible);
        owlObjectPropertyClass.setVisible(owlObjectPropertyWasVisible);
        owlDatatypePropertyClass.setVisible(owlDatatypePropertyWasVisible);

        if (type != null && !property.hasRDFType(type)) {
            final boolean isDatatype = type.getSuperclasses(true).contains(owlDatatypePropertyClass);
            if (property instanceof OWLObjectProperty && isDatatype) {
                property.setRange(null);
            }
            else {
                final boolean isObject = type.getSuperclasses(true).contains(owlObjectPropertyClass);
                if (property instanceof OWLDatatypeProperty && isObject) {
                    property.setRange(null);
                }
                else if (!(property instanceof OWLProperty) && (isDatatype || isObject)) {
                    property.setRange(null);
                }
            }
            property.setProtegeType((RDFSClass) type);
        }
    }


    private Cls getRootType(Slot slot) {
        OWLModel owlModel = (OWLModel) slot.getKnowledgeBase(); // Don't use getOWLModel()!
        if (ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.RDF) || !(slot instanceof OWLProperty)) {
            return owlModel.getRDFPropertyClass();
        }
        else {
            if (slot instanceof OWLDatatypeProperty) {
                return owlModel.getOWLDatatypePropertyClass();
            }
            else { // must be ObjectProperty
                return owlModel.getOWLObjectPropertyClass();
            }
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        if (resource.isEditable() && resource instanceof RDFProperty) {
            if (resource instanceof OWLProperty) {
                Collection types = Collections.singleton(getRootType((Slot) resource));
                return DisplayUtilities.hasMultipleConcreteClses(getOWLModel(), types);
            }
            else { // pure rdf:Property
                return true;
            }
        }
        return false;
    }
}
