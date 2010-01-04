package edu.stanford.smi.protegex.owl.inference.protegeowl.task.digreasoner;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.dig.translator.DIGQueryResponse;
import edu.stanford.smi.protegex.owl.inference.dig.translator.DIGTranslator;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import org.w3c.dom.Document;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 18, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class GetConceptIntersectionSuperclassesTask extends AbstractReasonerTask implements CollectionResultReasonerTask {

    private ProtegeOWLReasoner protegeOWLReasoner;

    private OWLClass[] clses;

    private HashSet result;


    public GetConceptIntersectionSuperclassesTask(OWLClass[] clses, ProtegeOWLReasoner protegeOWLReasoner) {
        super(protegeOWLReasoner);
        this.protegeOWLReasoner = protegeOWLReasoner;
        this.clses = clses;
        result = new HashSet();
    }


    public int getTaskSize() {
        return 1;
    }


    public void run()
            throws DIGReasonerException {
        setProgress(0);
        DIGTranslator translator = getTranslator();
        Document doc = translator.createAsksDocument(protegeOWLReasoner.getReasonerKnowledgeBaseURI());
        translator.createDirectSuperConceptsQuery(doc, "q0", clses);
        Document responseDoc = protegeOWLReasoner.getDIGReasoner().performRequest(doc);
        Iterator it = translator.getDIGQueryResponseIterator(protegeOWLReasoner.getKnowledgeBase(), responseDoc);
        while (it.hasNext()) {
            DIGQueryResponse response = (DIGQueryResponse) it.next();
            result.addAll(response.getConcepts());
        }
        setProgress(1);
        setTaskCompleted();
    }


    public Collection getResult() {
        return result;
    }
}

