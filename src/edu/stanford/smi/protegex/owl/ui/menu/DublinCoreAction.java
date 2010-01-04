package edu.stanford.smi.protegex.owl.ui.menu;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.actions.OWLModelActionConstants;
import edu.stanford.smi.protegex.owl.ui.repository.ShowRepositoryEditorAction;

import javax.swing.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DublinCoreAction extends AbstractOWLModelAction {

    public String getMenubarPath() {
        return OWL_MENU + PATH_SEPARATOR + OWLModelActionConstants.REPOSITORY_GROUP;
    }


    public String getName() {
        return "Dublin Core metadata...";
    }


    public void run(OWLModel owlModel) {
	    JOptionPane.showMessageDialog(ProtegeUI.getTopLevelContainer(owlModel.getProject()),
	                                  "This option has been removed.  Please add the Dublin Core\n" +
	                                  "import using the Metadata Tab.",
	                                  "Option removed",
	                                  JOptionPane.WARNING_MESSAGE);
    }
}
