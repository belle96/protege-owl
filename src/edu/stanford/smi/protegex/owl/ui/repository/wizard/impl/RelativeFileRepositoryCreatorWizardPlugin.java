package edu.stanford.smi.protegex.owl.ui.repository.wizard.impl;

import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.repository.wizard.RepositoryCreatorWizardPanel;
import edu.stanford.smi.protegex.owl.ui.repository.wizard.RepositoryCreatorWizardPlugin;


public class RelativeFileRepositoryCreatorWizardPlugin implements RepositoryCreatorWizardPlugin {

    public RelativeFileRepositoryCreatorWizardPlugin() {

    }


    public String getName() {
        return "Relative file";
    }


    public String getDescription() {
        return "Creates a file repository that is relative to the .owl file. Please " +
                "note that the project must be saved for this option to be available.";
    }


    public boolean isSuitable(OWLModel model) {
        if (model.getProject() != null) {
            return model.getProject().getProjectURI() != null;
        }
        else {
            return false;
        }

    }


    public RepositoryCreatorWizardPanel createRepositoryCreatorWizardPanel(WizardPage wizardPage,
                                                                           final OWLModel owlModel) {
        return new RelativeFileURLSpecificationWizardPanel(wizardPage, owlModel);
    }
}

