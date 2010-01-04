package edu.stanford.smi.protegex.owl.inference.protegeowl.task.digreasoner;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.dig.translator.DIGQueryResponse;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import org.w3c.dom.Document;

import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: May 6, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class GetSubsumptionRelationshipTask extends AbstractReasonerTask implements IntegerResultReasonerTask {


    private ProtegeOWLReasoner protegeOWLReasoner;

    private OWLClass cls1;

    private OWLClass cls2;

    private int result;


    public GetSubsumptionRelationshipTask(ProtegeOWLReasoner protegeOWLReasoner,
                                          OWLClass cls1,
                                          OWLClass cls2) {
        super(protegeOWLReasoner);
        this.protegeOWLReasoner = protegeOWLReasoner;
        this.cls1 = cls1;
        this.cls2 = cls2;
        result = 0;
    }


    public int getTaskSize() {
        return 1;
    }


    public void run()
            throws DIGReasonerException {
        Document asksDoc = getTranslator().createAsksDocument(protegeOWLReasoner.getReasonerKnowledgeBaseURI());
        getTranslator().createSubsumesQuery(asksDoc, "q1Sub2", cls1, cls2);
        getTranslator().createSubsumesQuery(asksDoc, "q2Sub1", cls2, cls1);
        Document responseDoc = protegeOWLReasoner.getDIGReasoner().performRequest(asksDoc);
        Iterator it = getTranslator().getDIGQueryResponseIterator(protegeOWLReasoner.getKnowledgeBase(), responseDoc);
        boolean cls1SubsumesCls2 = false;
        boolean cls2SubsumedCls1 = false;
        while (it.hasNext()) {
            DIGQueryResponse queryResponse = (DIGQueryResponse) it.next();
            if (queryResponse.getID().equals("q1Sub2")) {
                cls1SubsumesCls2 = queryResponse.getBoolean();
            }
            else {
                cls2SubsumedCls1 = queryResponse.getBoolean();
            }
        }
        if (cls1SubsumesCls2 == true) {
            if (cls2SubsumedCls1 == true) {
                result = ProtegeOWLReasoner.CLS1_EQUIVALENT_TO_CLS2;
            }
            else {
                result = ProtegeOWLReasoner.CLS1_SUBSUMES_CLS2;
            }
        }
        else {
            if (cls2SubsumedCls1 == true) {
                result = ProtegeOWLReasoner.CLS1_SUBSUMED_BY_CLS2;
            }
            else {
                result = ProtegeOWLReasoner.NO_SUBSUMPTION_RELATIONSHIP;
            }
        }
        setProgress(1);
        setTaskCompleted();
    }


    public int getResult() {
        return result;
    }
}

