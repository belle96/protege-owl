package edu.stanford.smi.protegex.owl.inference.protegeowl.task.digreasoner;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.dig.translator.DIGQueryResponse;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecordFactory;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import org.w3c.dom.Document;

import java.util.Collection;
import java.util.HashSet;
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
public class GetIndividualsBelongingToConceptTask extends AbstractReasonerTask implements CollectionResultReasonerTask {

    private OWLClass aClass;

    private ProtegeOWLReasoner protegeOWLReasoner;

    private HashSet individuals;


    public GetIndividualsBelongingToConceptTask(OWLClass aClass,
                                                ProtegeOWLReasoner protegeOWLReasoner) {
        super(protegeOWLReasoner);
        this.aClass = aClass;
        this.protegeOWLReasoner = protegeOWLReasoner;

        individuals = new HashSet();
    }


    public int getTaskSize() {
        return 1;
    }


    public void run() throws DIGReasonerException {
        OWLModel kb = protegeOWLReasoner.getKnowledgeBase();

        ReasonerLogRecordFactory logRecordFactory = ReasonerLogRecordFactory.getInstance();
        ReasonerLogRecord parentRecord = logRecordFactory.createInformationMessageLogRecord("Individuals belonging to: " + aClass.getBrowserText(),
                null);
        postLogRecord(parentRecord);
        setDescription("Computing individuals belonging to class");
        setMessage("Building reasoner query...");

        Document doc = getTranslator().createAsksDocument(protegeOWLReasoner.getReasonerKnowledgeBaseURI());
        getTranslator().createInstancesOfConceptQuery(doc, "q0", aClass);

        setMessage("Querying reasoner...");

        Document responseDoc = protegeOWLReasoner.getDIGReasoner().performRequest(doc);

        Iterator it = getTranslator().getDIGQueryResponseIterator(kb, responseDoc);


        while (it.hasNext()) {
            final DIGQueryResponse response = (DIGQueryResponse) it.next();

            individuals.addAll(response.getIndividuals());
        }

        setProgress(1);

        Iterator individualsIt = individuals.iterator();

        while (individualsIt.hasNext()) {
            postLogRecord(ReasonerLogRecordFactory.getInstance().createOWLInstanceLogRecord((RDFResource) individualsIt.next(),
                    parentRecord));
        }

        setTaskCompleted();
    }


    public Collection getResult() {
        return individuals;
    }
}

