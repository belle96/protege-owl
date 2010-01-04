package edu.stanford.smi.protegex.owl.ui.importstree;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.actions.OWLModelActionConstants;
import edu.stanford.smi.protegex.owl.ui.repository.ShowRepositoryEditorAction;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImportsTreeAction extends AbstractOWLModelAction {

    public String getMenubarPath() {
        return OWL_MENU + PATH_SEPARATOR + OWLModelActionConstants.REPOSITORY_GROUP;
    }


    public String getName() {
        return "Show owl:imports tree...";
    }


    public void run(OWLModel owlModel) {
        ImportsTreePanel.showDialog(owlModel);
    }
}
