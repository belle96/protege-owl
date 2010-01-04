package edu.stanford.smi.protegex.owl.ui.repository.wizard.impl;

import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.repository.wizard.RepositoryCreatorWizardPanel;
import edu.stanford.smi.protegex.owl.ui.repository.wizard.RepositoryCreatorWizardPlugin;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 3, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class HTTPRepositoryCreatorWizardPlugin implements RepositoryCreatorWizardPlugin {

    public String getName() {
        return "HTTP Repository";
    }


    public String getDescription() {
        return "Creates a repository that contains a specific ontology which is downloaded " +
                "from the web.";
    }


    public boolean isSuitable(OWLModel model) {
        return true;
    }


    public RepositoryCreatorWizardPanel createRepositoryCreatorWizardPanel(WizardPage wizardPage,
                                                                           OWLModel owlModel) {
        return new HTTPRepositoryCreatorWizardPanel(wizardPage);
    }
}

