package edu.stanford.smi.protegex.owl.ui.repository.wizard;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;

import javax.swing.*;
import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 26, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class PluginPanelHolder extends AbstractRepositoryWizardPage {

    private OWLModel owlModel;

    private RepositoryCreatorWizardPanel panel;


    public PluginPanelHolder(RepositoryWizard wizard, OWLModel owlModel) {
        super("Enter details", wizard);
        this.owlModel = owlModel;
        getContentComponent(). add(new JLabel("Please press the back button and select the type of repository."),
                BorderLayout.NORTH);
    }


	public void pageSelected() {
		RepositoryCreatorWizardPlugin plugin = getRepositoryWizard().getSelectedPlugin();
		if(plugin != null) {
			getContentComponent().removeAll();
			getContentComponent().add(panel = plugin.createRepositoryCreatorWizardPanel(this, owlModel));
		}
	}

    public Repository getRepository() {
        if (panel != null) {
            return panel.createRepository();
        }
        else {
            return null;
        }
    }
}



