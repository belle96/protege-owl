package edu.stanford.smi.protegex.owl.ui.repository.wizard.impl;

import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFolderRepository;
import edu.stanford.smi.protegex.owl.ui.repository.wizard.RepositoryCreatorWizardPanel;
import edu.stanford.smi.protegex.owl.ui.repository.wizard.RepositoryCreatorWizardPlugin;

import java.io.File;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 26, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class LocalFolderRepositoryCreatorWizardPlugin implements RepositoryCreatorWizardPlugin {

    public String getName() {
        return "Local folder";
    }


    public String getDescription() {
        return "Creates a repository that contains the ontologies that reside in a specified local folder.";
    }


    public boolean isSuitable(OWLModel model) {
        return true;
    }


    public RepositoryCreatorWizardPanel createRepositoryCreatorWizardPanel(WizardPage wizardPage,
                                                                           OWLModel owlModel) {
        return new LocalFolderRepositoryCreatorWizardPanel(wizardPage);
    }


    private class LocalFolderRepositoryCreatorWizardPanel extends FileSelectionWizardPanel {

        public LocalFolderRepositoryCreatorWizardPanel(WizardPage wizardPage) {
            super(wizardPage, true, "Select a <b>folder</b> that contains some OWL ontologies.  The ontologies contained" +
                    " in the selcted folder will be available to the system for importing.");
        }


        public Repository createRepository(File f, boolean forceReadOnly, boolean recursive) {
            return new LocalFolderRepository(f, forceReadOnly, recursive);
        }

    }
}

