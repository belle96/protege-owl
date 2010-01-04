package edu.stanford.smi.protegex.owl.ui.metadatatab.alldifferent;

import edu.stanford.smi.protege.ui.ProjectView;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.actions.OWLModelActionConstants;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

/**
 * An OWLModelAction to open a dialog to edit the owl:AllDifferents of a given OWLModel.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AllDifferentAction extends AbstractOWLModelAction {
			
	public String getMenubarPath() {
        return OWL_MENU + PATH_SEPARATOR + OWLModelActionConstants.MODEL_OPERATIONS_GROUP;
    }


    public String getName() {
        return "Edit owl:AllDifferents...";
    }


    public void run(OWLModel owlModel) {
        ProjectView projectView = ProtegeUI.getProjectView(owlModel);
        AllDifferentPanel panel = new AllDifferentPanel(owlModel);
        ProtegeUI.getModalDialogFactory().showDialog(projectView, panel, getName(), ModalDialogFactory.MODE_CLOSE);
    }
}
