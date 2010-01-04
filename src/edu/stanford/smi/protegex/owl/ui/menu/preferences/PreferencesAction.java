package edu.stanford.smi.protegex.owl.ui.menu.preferences;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.actions.OWLModelActionConstants;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

public class PreferencesAction extends AbstractOWLModelAction {


    public String getIconFileName() {
        return "Preferences";
    }


    public String getMenubarPath() {
        return OWL_MENU + PATH_SEPARATOR + OWLModelActionConstants.PREFERENCES_GROUP;
    }


    public String getName() {
        return "Preferences...";
    }


    public String getToolbarPath() {
        return null;
    }


    public void run(OWLModel owlModel) {
        PreferencesPanel panel = new PreferencesPanel(owlModel);
        ProtegeUI.getModalDialogFactory().showDialog(ProtegeUI.getTopLevelContainer(owlModel.getProject()), panel,
                "OWL Preferences", ModalDialogFactory.MODE_CLOSE);
        try {
            if (panel.getRequiresReloadUI()) {
                panel.ok();
                ProtegeUI.reloadUI(owlModel.getProject());
            }
        }
        catch (Exception ex) {
            // Ignore possible exception on closed KB
        }
    }
}
