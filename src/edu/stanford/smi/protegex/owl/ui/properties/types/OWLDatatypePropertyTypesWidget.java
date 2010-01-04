package edu.stanford.smi.protegex.owl.ui.properties.types;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.profiles.OWLProfiles;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfilesManager;

/**
 * A AbstractPropertyTypesWidget for OWLDatatypeProperties.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLDatatypePropertyTypesWidget extends AbstractPropertyTypesWidget {

    public void initialize() {
        OWLModel owlModel = getOWLModel();
        if (ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.OWL_Full)) {
            initialize(new RDFSNamedClass[]{
                    owlModel.getRDFSNamedClass(OWLNames.Cls.FUNCTIONAL_PROPERTY),
                    owlModel.getRDFSNamedClass(OWLNames.Cls.INVERSE_FUNCTIONAL_PROPERTY)
            });
        }
        else {
            initialize(new RDFSNamedClass[]{
                    owlModel.getRDFSNamedClass(OWLNames.Cls.FUNCTIONAL_PROPERTY)
            });
        }
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        if (cls instanceof RDFSNamedClass && slot.getName().equals(Model.Slot.DIRECT_TYPES)) {
            RDFSNamedClass t = (RDFSNamedClass) cls.getKnowledgeBase().getCls(OWLNames.Cls.DATATYPE_PROPERTY);
            return cls.equals(t) || cls.hasSuperclass(t);
        }
        return false;
    }
}
