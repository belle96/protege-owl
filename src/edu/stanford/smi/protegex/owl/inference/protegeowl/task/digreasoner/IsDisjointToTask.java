package edu.stanford.smi.protegex.owl.inference.protegeowl.task.digreasoner;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.dig.translator.DIGQueryResponse;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import org.w3c.dom.Document;

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

    private ProtegeOWLReasoner protegeOWLReasoner;

    private OWLClass cls1;

    private OWLClass cls2;

    private boolean result = false;


    public IsDisjointToTask(ProtegeOWLReasoner protegeOWLReasoner,
                            OWLClass cls1,
                            OWLClass cls2) {
        super(protegeOWLReasoner);
        this.protegeOWLReasoner = protegeOWLReasoner;
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
     * @throws edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException
     *
     */
    public void run()
            throws DIGReasonerException {
        OWLModel kb = protegeOWLReasoner.getKnowledgeBase();

        Document asksDoc = getTranslator().createAsksDocument(protegeOWLReasoner.getReasonerKnowledgeBaseURI());
        getTranslator().createDisjointQuery(asksDoc, "q0", cls1, cls2);

        Document responseDoc = protegeOWLReasoner.getDIGReasoner().performRequest(asksDoc);

        DIGQueryResponse response = (DIGQueryResponse) getTranslator().getDIGQueryResponseIterator(kb, responseDoc).next();

        result = response.getBoolean();
        setProgress(1);
        setTaskCompleted();
    }


    public boolean getResult() {
        return result;
    }
}

