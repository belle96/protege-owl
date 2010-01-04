package edu.stanford.smi.protegex.owl.ui.testing;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.testing.OWLTestManager;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.actions.OWLModelAction;
import edu.stanford.smi.protegex.owl.ui.actions.OWLModelActionConstants;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLModelTestAction extends AbstractOWLTestAction implements OWLModelAction {

    public AbstractOWLModelTestAction() {
    }


    public void dispose() {
    }


    public Class getIconResourceClass() {
        return OWLIcons.class;
    }


    public String getMenubarPath() {
    	return AbstractOWLModelAction.OWL_MENU + PATH_SEPARATOR + OWLModelActionConstants.OWL_TESTS_GROUP;
    }


	public String getToolbarPath() {
		return null;
	}


    public boolean isSuitable(OWLModel owlModel) {
        return true;
    }


    public void notifyPropertyChangeListeners(String propertyName, Object oldValue, Object newValue) {
        // TODO (theoretically)
    }


    public void run(OWLModel owlModel) {
        this.owlModel = owlModel;
        this.testManager = (OWLTestManager) owlModel;
        actionPerformed(null);
    }
}
