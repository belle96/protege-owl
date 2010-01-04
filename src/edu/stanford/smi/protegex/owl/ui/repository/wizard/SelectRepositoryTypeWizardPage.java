package edu.stanford.smi.protegex.owl.ui.repository.wizard;

import edu.stanford.smi.protegex.owl.model.OWLModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 26, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class SelectRepositoryTypeWizardPage extends AbstractRepositoryWizardPage {

    private ButtonGroup buttonGroup;

    private OWLModel owlModel;

	private RepositoryCreatorWizardPlugin selectedPlugin;


    public SelectRepositoryTypeWizardPage(RepositoryWizard wizard, OWLModel model) {
        super("Select repository type", wizard);
        this.owlModel = model;
        Box box = new Box(BoxLayout.Y_AXIS);
	    JPanel holder = new JPanel(new BorderLayout(7, 7));
        holder.add(new JLabel("Please select the type of repository that you would like to create"), BorderLayout.NORTH);
	    holder.add(box, BorderLayout.CENTER);
        buttonGroup = new ButtonGroup();
        String helpText = "";
        for (Iterator it = wizard.getPlugins().iterator(); it.hasNext();) {
            RepositoryCreatorWizardPlugin curPlugin = (RepositoryCreatorWizardPlugin) it.next();
            JRadioButton radioButton = new JRadioButton(new PluginSelectedAction(curPlugin));
            radioButton.setToolTipText(curPlugin.getDescription());
            radioButton.setEnabled(curPlugin.isSuitable(this.owlModel));
            box.add(radioButton);
            buttonGroup.add(radioButton);
            helpText += "<p>";
            helpText += "<b>" + curPlugin.getName() + "</b><font size=\"-2\"> " + curPlugin.getDescription();
            helpText += "</font></p>";


        }
	    box.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        getContentComponent().add(holder, BorderLayout.NORTH);
        setHelpText("Please select the type of repository that you would like to add:", helpText);
    }


    private class PluginSelectedAction extends AbstractAction {

        private RepositoryCreatorWizardPlugin plugin;


        public PluginSelectedAction(RepositoryCreatorWizardPlugin plugin) {
            super(plugin.getName());
            this.plugin = plugin;

        }


        public void actionPerformed(ActionEvent e) {
            selectedPlugin = this.plugin;
        }
    }

	public void nextPressed() {
		getRepositoryWizard().setSelectedPlugin(selectedPlugin);
	}


    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }

}

