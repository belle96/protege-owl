package edu.stanford.smi.protegex.owl.ui.wizard;

import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Dec 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLWizardPage extends WizardPage {

	private JComponent contentComponent;

	private JComponent helpComponent;

	private static final String HELP_PANEL_LOCATION = BorderLayout.SOUTH;

	public OWLWizardPage(String name, OWLWizard wizard) {
		super(name, wizard);
		contentComponent = new JPanel(new BorderLayout());
		setLayout(new BorderLayout());
		add(contentComponent);
		helpComponent = new JPanel();
		add(helpComponent, BorderLayout.SOUTH);
	}

	public void nextPressed() {
		// Do nothing
	}

	public void prevPressed() {
		// Do nothing
	}

	public void pageSelected() {
		// Do nothing
	}

	public JComponent getContentComponent() {
		return contentComponent;
	}

	public void setHelpText(String title, String text) {
		if(helpComponent != null) {
			remove(helpComponent);
		}
		if(text != null) {
			helpComponent = OWLUI.createHelpPanel(text, title);
		}
		else {
			helpComponent = new JPanel();
		}
		add(helpComponent, HELP_PANEL_LOCATION);
		revalidate();
	}

	public OWLWizard getOWLWizard() {
		return (OWLWizard) getWizard();
	}
}

