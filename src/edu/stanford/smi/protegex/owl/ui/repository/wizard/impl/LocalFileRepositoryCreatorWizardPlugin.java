package edu.stanford.smi.protegex.owl.ui.repository.wizard.impl;

import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFileRepository;
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
public class LocalFileRepositoryCreatorWizardPlugin implements RepositoryCreatorWizardPlugin {

    public String getName() {
        return "Local file";
    }


    public String getDescription() {
        return "Creates a repository that contains the ontology that is contained in a specific file.";
    }


    public boolean isSuitable(OWLModel model) {
        return true;
    }


    public RepositoryCreatorWizardPanel createRepositoryCreatorWizardPanel(WizardPage wizardPage,
                                                                           OWLModel owlModel) {
        return new FileSelectionWizardPanel(wizardPage, false, "Select a <b>file</b> that contains an OWL ontology " +
                "(file ending with a .owl extension).  The ontology that " +
                "that is contained in this file will be available to the " +
                "system for importing.") {
            public Repository createRepository(File f, boolean forceReadOnly, boolean recursive) {
                return new LocalFileRepository(f, forceReadOnly);
            }
        };
    }
}

