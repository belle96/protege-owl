package edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecordFactory;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;

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

    private ProtegeReasoner protegeReasoner;

    private HashSet parents;

    private String taskDesciption;


    public AbstractSingleConceptWithConceptCollectionResultTask(String taskDescription,
                                                                OWLClass aClass,
                                                                ProtegeReasoner protegeReasoner) {
        super(protegeReasoner);
        this.taskDesciption = taskDescription;
        this.aClass = aClass;
        this.protegeReasoner = protegeReasoner;

        parents = new HashSet();
    }


    public int getTaskSize() {
        return 1;
    }


    public void run() throws ProtegeReasonerException {
        setDescription(taskDesciption);
        setMessage("Building reasoner query...");
        setProgress(0);
        doAbortCheck();

        setMessage("Querying reasoner...");

        parents.addAll(getQueryResults());

        ReasonerLogRecordFactory factory = ReasonerLogRecordFactory.getInstance();
        ReasonerLogRecord parentRecord = factory.createInformationMessageLogRecord("Concepts", null);
        postLogRecord(parentRecord);

        Iterator it = parents.iterator();
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
     * It will be called by the run method.   
     *
     * @throws ProtegeReasonerException
     */
    public abstract Collection getQueryResults() throws ProtegeReasonerException;


    protected OWLClass getCls() {
        return aClass;
    }


    protected ProtegeReasoner getProtegeReasoner() {
        return protegeReasoner;
    }
}

