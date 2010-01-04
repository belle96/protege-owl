package edu.stanford.smi.protegex.owl.ui.repository;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.actions.OWLModelActionConstants;

import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 18, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ShowRepositoryEditorAction extends AbstractOWLModelAction {

    public final static String GROUP = OWLModelActionConstants.REPOSITORY_GROUP;


    public String getIconFileName() {
        return "AddFromOntPolicy";
    }


    public String getMenubarPath() {
        return OWL_MENU + PATH_SEPARATOR + GROUP;
    }


    public String getName() {
        return "Ontology repositories...";
    }


    public void run(OWLModel owlModel) {
        Component f = ProtegeUI.getTopLevelContainer(owlModel.getProject());
        RepositoryManagerPanel.showDialog(f, owlModel);
    }
}

