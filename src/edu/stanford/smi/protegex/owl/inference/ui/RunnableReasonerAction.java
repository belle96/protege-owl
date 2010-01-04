package edu.stanford.smi.protegex.owl.inference.ui;

import edu.stanford.smi.protegex.owl.inference.protegeowl.task.ReasonerTaskListener;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 23, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface RunnableReasonerAction {

    /**
     * Will be called by the runner to execute some reasoner actions.
     *
     * @param taskListener
     * @throws ProtegeReasonerException
     */
    void executeReasonerActions(ReasonerTaskListener taskListener) throws ProtegeReasonerException;


    OWLModel getOWLModel();
}
