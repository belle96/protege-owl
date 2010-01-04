package edu.stanford.smi.protegex.owl.inference.ui.inspector;

import edu.stanford.smi.protegex.owl.inference.ui.action.ActionConstants;
import edu.stanford.smi.protegex.owl.inference.ui.icons.InferenceIcons;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 15, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ShowReasonerInspectorAction extends AbstractOWLModelAction {

    public Class getIconResourceClass() {
        return InferenceIcons.class;
    }


    public String getIconFileName() {
        return InferenceIcons.REASONER_INSPECTOR;
    }


    public String getMenubarPath() {
        return REASONING_MENU + PATH_SEPARATOR + ActionConstants.ACTION_GROUP;
    }


    public String getName() {
        return "Reasoner inspector...";
    }


    public void run(OWLModel owlModel) {
        ProtegeUI.getModalDialogFactory().showDialog(ProtegeUI.getTopLevelContainer(owlModel.getProject()),
                new ReasonerInspectorPanel(owlModel), "Reasoner Inspector", ModalDialogFactory.MODE_CLOSE);
    }
}

