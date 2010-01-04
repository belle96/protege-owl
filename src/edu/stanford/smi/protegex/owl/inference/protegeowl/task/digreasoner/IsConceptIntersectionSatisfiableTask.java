package edu.stanford.smi.protegex.owl.inference.protegeowl.task.digreasoner;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.dig.translator.DIGQueryResponse;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import org.w3c.dom.Document;

import java.util.Iterator;

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

    private ProtegeOWLReasoner protegeOWLReasoner;

    private boolean satisfiable;


    public IsConceptIntersectionSatisfiableTask(OWLClass[] clses,
                                                ProtegeOWLReasoner protegeOWLReasoner) {
        super(protegeOWLReasoner);
        this.clses = clses;
        this.protegeOWLReasoner = protegeOWLReasoner;
        satisfiable = true;
    }


    public int getTaskSize() {
        return 1;
    }


    public void run() throws DIGReasonerException {
        OWLModel kb = protegeOWLReasoner.getKnowledgeBase();

        setProgress(0);
        setDescription("Computing satisfiability of concept intersection");
        setMessage("Building reasoner query...");

        Document asksDocument = getTranslator().createAsksDocument(protegeOWLReasoner.getReasonerKnowledgeBaseURI());
        getTranslator().createSatisfiableQuery(asksDocument, "q0", clses);

        setMessage("Querying reasoner...");

        Document responseDoc = protegeOWLReasoner.getDIGReasoner().performRequest(asksDocument);

        Iterator responseIt = getTranslator().getDIGQueryResponseIterator(kb, responseDoc);

        if (responseIt.hasNext()) {
            final DIGQueryResponse response = (DIGQueryResponse) responseIt.next();

            satisfiable = response.getBoolean();
        }

        setMessage("Complete");
        setProgress(1);
        setTaskCompleted();
    }


    public boolean getResult() {
        return satisfiable;
    }
}

