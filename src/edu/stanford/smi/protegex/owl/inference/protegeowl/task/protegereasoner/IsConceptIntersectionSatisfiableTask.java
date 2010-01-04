package edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner;

import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLClass;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 17, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class IsConceptIntersectionSatisfiableTask extends AbstractReasonerTask implements BooleanResultReasonerTask {

    private OWLClass[] clses;

    private ProtegeReasoner protegeReasoner;

    private boolean satisfiable;


    public IsConceptIntersectionSatisfiableTask(OWLClass[] clses,
                                                ProtegeReasoner protegeReasoner) {
        super(protegeReasoner);
        this.clses = clses;
        this.protegeReasoner = protegeReasoner;
        satisfiable = true;
    }


    public int getTaskSize() {
        return 1;
    }


    public void run() throws ProtegeReasonerException {              
        setProgress(0);
        setDescription("Computing satisfiability of concept intersection");
        
        setMessage("Querying reasoner...");

        satisfiable = protegeReasoner.isIntersectionSatisfiable(clses);

        setMessage("Complete");
        setProgress(1);
        setTaskCompleted();
    }


    public boolean getResult() {
        return satisfiable;
    }
}

