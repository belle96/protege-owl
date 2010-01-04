package edu.stanford.smi.protegex.owl.ui.properties.types;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

import java.util.Collection;

/**
 * A AbstractPropertyTypesWidget for OWLObjectProperties.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLObjectPropertyTypesWidget extends AbstractPropertyTypesWidget {


    private void alignRangeAndDomain() {
        OWLObjectProperty property = (OWLObjectProperty) getEditedResource();
        if (property.isSymmetric()) {
            Collection domain = property.getUnionDomain();
            Collection range;
            range = property.getUnionRangeClasses();
            if (!domain.containsAll(range) || !range.containsAll(domain)) {
                //JOptionPane.showMessageDialog(this, "Symmetric properties should have identical\ndomains and ranges.");
            }
        }
    }


    public void initialize() {
        OWLModel owlModel = getOWLModel();
        initialize(new RDFSNamedClass[]{
                owlModel.getRDFSNamedClass(OWLNames.Cls.FUNCTIONAL_PROPERTY),
                owlModel.getRDFSNamedClass(OWLNames.Cls.INVERSE_FUNCTIONAL_PROPERTY),
                owlModel.getRDFSNamedClass(OWLNames.Cls.SYMMETRIC_PROPERTY),
                owlModel.getRDFSNamedClass(OWLNames.Cls.TRANSITIVE_PROPERTY)
        });
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        if (cls instanceof RDFSNamedClass && slot.getName().equals(Model.Slot.DIRECT_TYPES)) {
            RDFSNamedClass t = (RDFSNamedClass) cls.getKnowledgeBase().getCls(OWLNames.Cls.OBJECT_PROPERTY);
            return cls.equals(t) || cls.hasSuperclass(t);
        }
        return false;
    }


    protected void postProcessChange(RDFSNamedClass type) {
        super.postProcessChange(type);
        if (type.equals(getOWLModel().getRDFResource(OWLNames.Cls.SYMMETRIC_PROPERTY))) {
            alignRangeAndDomain();
            updateInverseProperty();
        }
    }


    private void updateInverseProperty() {
        OWLObjectProperty property = (OWLObjectProperty) getEditedResource();
        if (property.isSymmetric()) {
            property.setInverseProperty(property);
        }
        else {
            property.setInverseProperty(null);
        }
    }
}
