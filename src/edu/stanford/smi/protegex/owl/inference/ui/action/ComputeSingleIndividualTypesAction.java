package edu.stanford.smi.protegex.owl.inference.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Collection;

import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager;
import edu.stanford.smi.protegex.owl.inference.protegeowl.task.ReasonerTaskListener;
import edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner.GetIndividualInferredTypesTask;
import edu.stanford.smi.protegex.owl.inference.reasoner.AbstractProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.inference.ui.ReasonerActionRunner;
import edu.stanford.smi.protegex.owl.inference.ui.RunnableReasonerAction;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividual;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 17, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ComputeSingleIndividualTypesAction extends ResourceAction implements RunnableReasonerAction {

    private Collection inferredTypes;


    public ComputeSingleIndividualTypesAction() {
        super("Compute types", OWLIcons.getComputeSingleInferredTypesIcon(), ActionConstants.ACTION_GROUP);
    }


    public boolean isSuitable(Component component,
                              RDFResource resource) {
        if (resource instanceof RDFIndividual &&
                !(resource instanceof SWRLIndividual)) {
            return !resource.isAnonymous();
        }
        else {
            return false;
        }
    }


    public void actionPerformed(ActionEvent e) {
        ReasonerActionRunner runner = new ReasonerActionRunner(this, false);
        runner.execute();
    }


    public void executeReasonerActions(ReasonerTaskListener taskListener) throws ProtegeReasonerException {
        ProtegeReasoner reasoner= ReasonerManager.getInstance().getProtegeReasoner(getOWLModel());
        reasoner.setReasonerTaskListener(taskListener);
        
        //ugly handling of different dig vs. direct reasoner - for backwards compatibility reasons
        if (reasoner instanceof AbstractProtegeReasoner && !(reasoner instanceof ProtegeOWLReasoner)) {
        	AbstractProtegeReasoner protegeReasoner = (AbstractProtegeReasoner) reasoner;
        	GetIndividualInferredTypesTask task = new GetIndividualInferredTypesTask((OWLIndividual) getResource(), reasoner);
    		protegeReasoner.performTask(task);
    		inferredTypes = task.getResult();
    		return;
		} else {
			inferredTypes = reasoner.getIndividualTypes((OWLIndividual) getResource());
        }
        
    }
}

