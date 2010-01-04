package edu.stanford.smi.protegex.owl.ui.metadata;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.profiles.OWLProfiles;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfilesManager;


/**
 * The MetadataWidget for NamedClses.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
@SuppressWarnings("deprecation")
public class RDFSNamedClassMetadataWidget extends OWLMetadataWidget {

    protected void createExtraWidgets() {
        // Suppress sameAs and differentFrom:
    }


    protected void setupSubWidgets(Slot slot, Cls cls, boolean isDesignTime, Project project) {
        OWLModel owlModel = (OWLModel) project.getKnowledgeBase();
        if (ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.SameAs_between_Classes)) {
            createSameAsWidget();
        }
        if (ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.DifferentFrom_between_Classes)) {
            createDifferentFromWidget();
        }
        super.setupSubWidgets(slot, cls, isDesignTime, project);
    }
}
