package edu.stanford.smi.protegex.owl.inference.protegeowl.task.digreasoner;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.dig.translator.DIGQueryResponse;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecordFactory;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
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
 * <p/>
 * An abstract base class for task that take a single
 * concept as a parameter, and return a collection of
 * concepts as a result.
 */
public abstract class AbstractSingleConceptWithConceptCollectionResultTask extends AbstractReasonerTask implements CollectionResultReasonerTask {

    private OWLClass aClass;

    private ProtegeOWLReasoner protegeOWLReasoner;

    private HashSet parents;

    private String taskDesciption;


    public AbstractSingleConceptWithConceptCollectionResultTask(String taskDescription,
                                                                OWLClass aClass,
                                                                ProtegeOWLReasoner protegeOWLReasoner) {
        super(protegeOWLReasoner);
        this.taskDesciption = taskDescription;
        this.aClass = aClass;
        this.protegeOWLReasoner = protegeOWLReasoner;

        parents = new HashSet();
    }


    public int getTaskSize() {
        return 1;
    }


    public void run() throws DIGReasonerException {
        setDescription(taskDesciption);
        setMessage("Building reasoner query...");
        setProgress(0);
        doAbortCheck();

        Document doc = getTranslator().createAsksDocument(protegeOWLReasoner.getReasonerKnowledgeBaseURI());
        createQuery(doc);

        setMessage("Querying reasoner...");
        Document responseDoc = protegeOWLReasoner.getDIGReasoner().performRequest(doc);
        doAbortCheck();

        Iterator it = getTranslator().getDIGQueryResponseIterator(protegeOWLReasoner.getKnowledgeBase(),
                responseDoc);

        while (it.hasNext()) {
            final DIGQueryResponse response = (DIGQueryResponse) it.next();
            parents.addAll(response.getConcepts());
        }

        ReasonerLogRecordFactory factory = ReasonerLogRecordFactory.getInstance();
        ReasonerLogRecord parentRecord = factory.createInformationMessageLogRecord("Concepts", null);
        postLogRecord(parentRecord);

        it = parents.iterator();
        while (it.hasNext()) {
            postLogRecord(factory.createOWLInstanceLogRecord((RDFResource) it.next(), parentRecord));
        }

        setProgress(1);
        doAbortCheck();
        setTaskCompleted();
    }


    public Collection getResult() {
        return parents;
    }


    /**
     * This method must be implemented by concrete subclasses.
     * It will be called by the run method in order to generate
     * the DIGQuery.
     *
     * @param doc The base <code>Document</code> for the query.
     * @throws DIGReasonerException
     */
    public abstract void createQuery(Document doc) throws DIGReasonerException;


    protected RDFSClass getCls() {
        return aClass;
    }


    protected ProtegeOWLReasoner getProtegeOWLReasoner() {
        return protegeOWLReasoner;
    }
}

