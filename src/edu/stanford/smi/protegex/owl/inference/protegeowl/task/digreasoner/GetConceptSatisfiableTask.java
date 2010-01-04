package edu.stanford.smi.protegex.owl.inference.protegeowl.task.digreasoner;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.dig.translator.DIGQueryResponse;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecordFactory;
import edu.stanford.smi.protegex.owl.inference.util.TimeDifference;
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
public class GetConceptSatisfiableTask extends AbstractReasonerTask implements BooleanResultReasonerTask {

    private OWLClass aClass;

    private ProtegeOWLReasoner protegeOWLReasoner;

    private boolean satisfiable;


    public GetConceptSatisfiableTask(OWLClass aClass,
                                     ProtegeOWLReasoner protegeOWLReasoner) {
        super(protegeOWLReasoner);
        this.aClass = aClass;
        this.protegeOWLReasoner = protegeOWLReasoner;
        this.satisfiable = true;
    }


    public int getTaskSize() {
        return 1;
    }


    public void run() throws DIGReasonerException {
        OWLModel kb = protegeOWLReasoner.getKnowledgeBase();

        ReasonerLogRecordFactory logRecordFactory = ReasonerLogRecordFactory.getInstance();
        ReasonerLogRecord parentRecord = logRecordFactory.createInformationMessageLogRecord("Checking consistency of " + aClass.getBrowserText(),
                null);
        postLogRecord(parentRecord);
        setDescription("Computing consistency");
        setMessage("Building reasoner query...");

        Document doc = getTranslator().createAsksDocument(protegeOWLReasoner.getReasonerKnowledgeBaseURI());
        getTranslator().createSatisfiableQuery(doc, "q0", aClass);

        setMessage("Querying reasoner...");
        TimeDifference td = new TimeDifference();
        td.markStart();

        Document responseDoc = protegeOWLReasoner.getDIGReasoner().performRequest(doc);

        td.markEnd();
        postLogRecord(logRecordFactory.createInformationMessageLogRecord("Time to query reasoner = " + td,
                parentRecord));

        Iterator it = getTranslator().getDIGQueryResponseIterator(kb, responseDoc);

        if (it.hasNext()) {
            DIGQueryResponse response = (DIGQueryResponse) it.next();

            satisfiable = response.getBoolean();
        }
        postLogRecord(logRecordFactory.createConceptConsistencyLogRecord(aClass, satisfiable, parentRecord));
        setProgress(1);
        setMessage("Finished");
        setTaskCompleted();
    }


    public boolean getResult() {
        return satisfiable;
    }
}

