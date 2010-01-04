package edu.stanford.smi.protegex.owl.inference.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager;
import edu.stanford.smi.protegex.owl.inference.protegeowl.task.ReasonerTaskListener;
import edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner.GetIndividualPropertyValuesTask;
import edu.stanford.smi.protegex.owl.inference.reasoner.AbstractProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.inference.ui.ReasonerActionRunner;
import edu.stanford.smi.protegex.owl.inference.ui.RunnableReasonerAction;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;

public class GetSingleIndividualPropertyValuesAction extends ResourceAction implements RunnableReasonerAction {

	public GetSingleIndividualPropertyValuesAction() {
		super("Get inferred property values", null, ActionConstants.ACTION_GROUP);
	}

	@Override
    public boolean isSuitable(Component component, RDFResource frame) {
    	return (frame instanceof OWLIndividual);
    }


	public void executeReasonerActions(ReasonerTaskListener taskListener) throws ProtegeReasonerException {
	       ProtegeReasoner reasoner = ReasonerManager.getInstance().getProtegeReasoner(getResource().getOWLModel());
	        reasoner.setReasonerTaskListener(taskListener);
	   	        
	        //ugly handling of different dig vs. direct reasoner
	        if (reasoner instanceof AbstractProtegeReasoner && !(reasoner instanceof ProtegeOWLReasoner)) {
	        	AbstractProtegeReasoner protegeReasoner = (AbstractProtegeReasoner) reasoner;
	        	GetIndividualPropertyValuesTask task = new GetIndividualPropertyValuesTask((OWLIndividual) getResource(), reasoner);
	    		protegeReasoner.performTask(task);
	    		return;
			} else {
				//FIXME
	        	
	        }	       
	}
	

    public void actionPerformed(ActionEvent e) {
        ReasonerActionRunner runner = new ReasonerActionRunner(this, false);
        runner.execute();
    }
}