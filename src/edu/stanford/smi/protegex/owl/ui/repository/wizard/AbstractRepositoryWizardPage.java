package edu.stanford.smi.protegex.owl.ui.repository.wizard;

import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizardPage;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Dec 8, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class AbstractRepositoryWizardPage extends OWLWizardPage {


	public AbstractRepositoryWizardPage(String name, RepositoryWizard wizard) {
		super(name, wizard);
	}

	public RepositoryWizard getRepositoryWizard() {
		return (RepositoryWizard) getWizard();
	}
}

