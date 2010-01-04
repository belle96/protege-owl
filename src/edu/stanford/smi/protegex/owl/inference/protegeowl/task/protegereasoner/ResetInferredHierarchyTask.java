package edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 19, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ResetInferredHierarchyTask extends AbstractReasonerTask {

    private ProtegeReasoner protegeReasoner;


    public ResetInferredHierarchyTask(ProtegeReasoner protegeReasoner) {
        super(protegeReasoner);
        this.protegeReasoner = protegeReasoner;
    }


    public int getTaskSize() {
        return 1;
    }


    public void run() throws DIGReasonerException {
        setDescription("Resetting inferred hierarchy");
        setMessage("Clearing...");
        setProgressIndeterminate(true);

        OWLModel kb = protegeReasoner.getOWLModel();
        boolean eventsEnabled = kb.setGenerateEventsEnabled(false);
       
        try {
        	kb.beginTransaction("Reset inferred hierarchy");
        	OWLUtil.resetComputedSuperclasses(kb);        	
        	kb.commitTransaction();
        } catch (Exception e) {
        	kb.rollbackTransaction();
        	Log.getLogger().warning("Exception in transaction. Rollback. Exception: " + e.getMessage());
        	RuntimeException re = new RuntimeException();
        	re.initCause(e);
        	throw re;
        }
        kb.setGenerateEventsEnabled(eventsEnabled);

        setProgressIndeterminate(false);
        setTaskCompleted();
        setProgress(1);
    }
}

