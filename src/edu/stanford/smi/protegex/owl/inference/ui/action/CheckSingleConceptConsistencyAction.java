package edu.stanford.smi.protegex.owl.inference.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager;
import edu.stanford.smi.protegex.owl.inference.protegeowl.task.ReasonerTaskListener;
import edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner.GetConceptSatisfiableTask;
import edu.stanford.smi.protegex.owl.inference.reasoner.AbstractProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.inference.ui.ReasonerActionRunner;
import edu.stanford.smi.protegex.owl.inference.ui.RunnableReasonerAction;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jul 21, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class CheckSingleConceptConsistencyAction extends ResourceAction implements RunnableReasonerAction {

    public CheckSingleConceptConsistencyAction() {
        super("Check concept consistency", OWLIcons.getCheckConsistencyIcon(), ActionConstants.ACTION_GROUP);
    }


    public boolean isSuitable(Component component, RDFResource frame) {
        if (frame instanceof OWLClass) {
            return true;
        }
        return false;
    }


    public void actionPerformed(ActionEvent e) {
        ReasonerActionRunner runner = new ReasonerActionRunner(this, false);
        runner.execute();
    }


    public void executeReasonerActions(ReasonerTaskListener taskListener) throws ProtegeReasonerException {
        ProtegeReasoner reasoner = ReasonerManager.getInstance().getProtegeReasoner(getResource().getOWLModel());
        reasoner.setReasonerTaskListener(taskListener);
        
        //ugly handling of different dig vs. direct reasoner - for backwards compatibility reasons
		if (reasoner instanceof AbstractProtegeReasoner && !(reasoner instanceof ProtegeOWLReasoner)) {
			AbstractProtegeReasoner protegeReasoner = (AbstractProtegeReasoner) reasoner;
        	GetConceptSatisfiableTask task = new GetConceptSatisfiableTask((OWLClass) getResource(), reasoner);
    		protegeReasoner.performTask(task);
        } else {
        	reasoner.isSatisfiable((OWLClass) getResource());
        }
       
    }
}

