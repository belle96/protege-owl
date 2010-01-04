package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;



/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Dec 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class AbstractImportStartWizardPage extends AbstractImportWizardPage {

	public AbstractImportStartWizardPage(String name, ImportWizard wizard) {
		super(name, wizard);
	}


	public void pageSelected() {
		getImportWizard().reset();
		super.pageSelected();
	}
}

