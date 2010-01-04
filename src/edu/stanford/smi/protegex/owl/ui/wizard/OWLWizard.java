package edu.stanford.smi.protegex.owl.ui.wizard;

import edu.stanford.smi.protege.util.Wizard;

import javax.swing.*;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Dec 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 *
 * Overrides the wizard to add notification to
 * wizard pages for next and prev button pressed.
 */
public class OWLWizard extends Wizard {

	public static final int DEFAULT_WIDTH = 600;

	public static final int DEFAULT_HEIGHT = 500;

	public OWLWizard(JComponent parent, String title) {
		super(parent, title);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}


	protected void showNextPage() {
		((OWLWizardPage) getCurrentPage()).nextPressed();
		super.showNextPage();
		((OWLWizardPage) getCurrentPage()).pageSelected();
	}


	protected void showPreviousPage() {
		((OWLWizardPage) getCurrentPage()).prevPressed();
		super.showPreviousPage();
		((OWLWizardPage) getCurrentPage()).pageSelected();
	}
}

