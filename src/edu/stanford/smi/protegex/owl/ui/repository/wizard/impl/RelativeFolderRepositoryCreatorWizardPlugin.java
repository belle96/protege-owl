package edu.stanford.smi.protegex.owl.ui.repository.wizard.impl;

import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.repository.wizard.RepositoryCreatorWizardPanel;
import edu.stanford.smi.protegex.owl.ui.repository.wizard.RepositoryCreatorWizardPlugin;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 28, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RelativeFolderRepositoryCreatorWizardPlugin implements RepositoryCreatorWizardPlugin {

    public RelativeFolderRepositoryCreatorWizardPlugin() {

    }


    public String getName() {
        return "Relative folder";
    }


    public String getDescription() {
        return "Creates a folder repository that is relative to the .owl file. Please " +
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
        return new RelativeURLSpecificationWizardPanel(wizardPage, owlModel);
    }
}

