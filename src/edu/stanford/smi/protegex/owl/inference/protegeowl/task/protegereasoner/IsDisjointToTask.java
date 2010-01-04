package edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner;

import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLClass;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 12, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class IsDisjointToTask extends AbstractReasonerTask implements BooleanResultReasonerTask {

    private ProtegeReasoner protegeReasoner;

    private OWLClass cls1;

    private OWLClass cls2;

    private boolean result = false;


    public IsDisjointToTask(ProtegeReasoner protegeReasoner,
                            OWLClass cls1,
                            OWLClass cls2) {
        super(protegeReasoner);
        this.protegeReasoner = protegeReasoner;
        this.cls1 = cls1;
        this.cls2 = cls2;
    }


    /**
     * Gets the size of the task.  When the progress
     * reaches this size, the task should be complete.
     */
    public int getTaskSize() {
        return 1;
    }


    /**
     * Executes the task.
     *
     * @throws ProtegeReasonerException
     *
     */
    public void run() throws ProtegeReasonerException {    	
        result = protegeReasoner.isDisjointTo(cls1, cls2);
        
        setProgress(1);
        setMessage("Finished");
        setTaskCompleted();
    }


    public boolean getResult() {
        return result;
    }
}

