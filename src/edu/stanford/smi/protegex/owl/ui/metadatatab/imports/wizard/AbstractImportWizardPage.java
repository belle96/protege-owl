package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizardPage;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Dec 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class AbstractImportWizardPage extends OWLWizardPage {

	public AbstractImportWizardPage(String name, ImportWizard importWizard) {
		super(name, importWizard);
	}

	protected ImportWizard getImportWizard() {
		return (ImportWizard) getWizard();
	}
}

