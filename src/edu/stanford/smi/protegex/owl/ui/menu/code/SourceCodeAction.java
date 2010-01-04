package edu.stanford.smi.protegex.owl.ui.menu.code;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.actions.OWLModelActionConstants;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import java.awt.*;

/**
 * An Action to display a SourceCodeDialog.
 *
 * @author Daniel Stoeckli <stoeckli@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SourceCodeAction extends AbstractOWLModelAction {


    public String getIconFileName() {
        return OWLIcons.SOURCE_CODE;
    }


    public String getMenubarPath() {
        return CODE_MENU + PATH_SEPARATOR + OWLModelActionConstants.ONT_LANGUAGE_GROUP;
    }


    public String getName() {
        return "Show RDF/XML source code...";
    }


    public boolean isSuitable(OWLModel owlModel) {
        return owlModel instanceof JenaOWLModel;
    }


    public void run(OWLModel owlModel) {
        Component parent = ProtegeUI.getTopLevelContainer(owlModel.getProject());
        ProtegeUI.getModalDialogFactory().showDialog(parent, new SourceCodePanel(owlModel),
                "RDF/XML Source Code", ModalDialogFactory.MODE_CLOSE);
    }
}
