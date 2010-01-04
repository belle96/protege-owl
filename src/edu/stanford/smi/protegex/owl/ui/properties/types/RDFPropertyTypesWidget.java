package edu.stanford.smi.protegex.owl.ui.properties.types;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFNames;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.profiles.OWLProfiles;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfilesManager;

/**
 * A AbstractPropertyTypesWidget for OWLDatatypeProperties.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFPropertyTypesWidget extends AbstractPropertyTypesWidget {

    public void initialize() {
        OWLModel owlModel = getOWLModel();
        initialize(new RDFSNamedClass[]{
                owlModel.getRDFSNamedClass(OWLNames.Cls.FUNCTIONAL_PROPERTY)
        });
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        if (cls instanceof RDFSNamedClass && slot.getName().equals(Model.Slot.DIRECT_TYPES)) {
            RDFSNamedClass t = (RDFSNamedClass) cls.getKnowledgeBase().getCls(RDFNames.Cls.PROPERTY);
            return cls.equals(t);
        }
        return false;
    }


    public void setEditable(boolean b) {
        if (ProfilesManager.isFeatureSupported(getOWLModel(), OWLProfiles.RDF_but_not_OWL) &&
                !ProfilesManager.isFeatureSupported(getOWLModel(), OWLProfiles.OWL_Lite)) {
            b = false;
        }
        super.setEditable(b);
    }
}
