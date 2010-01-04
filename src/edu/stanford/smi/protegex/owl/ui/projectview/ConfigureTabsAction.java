package edu.stanford.smi.protegex.owl.ui.projectview;

import edu.stanford.smi.protege.ui.ProjectView;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * An Action to configure the tabs in the current ProjectView.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConfigureTabsAction extends AbstractAction {

    private ProjectView projectView;


    public ConfigureTabsAction(ProjectView projectView) {
        super("Configure tabs...", OWLIcons.getImageIcon(OWLIcons.SELECT_FILE));
        this.projectView = projectView;
    }


    public void actionPerformed(ActionEvent e) {
        ConfigureTabsPanel panel = new ConfigureTabsPanel(projectView);
        if (ProtegeUI.getModalDialogFactory().showDialog(projectView, panel, NAME, ModalDialogFactory.MODE_OK_CANCEL) == ModalDialogFactory.OPTION_OK) {
            panel.saveContents();
            ProtegeUI.reloadUI(projectView);
        }
    }
}
