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
 */
public class GetIndividualsBelongingToConceptTask extends AbstractReasonerTask implements CollectionResultReasonerTask {

    private OWLClass aClass;

    private ProtegeReasoner protegeReasoner;

    private HashSet individuals;


    public GetIndividualsBelongingToConceptTask(OWLClass aClass,
                                                ProtegeReasoner protegeReasoner) {
        super(protegeReasoner);
        this.aClass = aClass;
        this.protegeReasoner = protegeReasoner;

        individuals = new HashSet();
    }


    public int getTaskSize() {
        return 1;
    }


    public void run() throws ProtegeReasonerException {     

        ReasonerLogRecordFactory logRecordFactory = ReasonerLogRecordFactory.getInstance();
        ReasonerLogRecord parentRecord = logRecordFactory.createInformationMessageLogRecord("Individuals belonging to: " + aClass.getBrowserText(),
                null);
        postLogRecord(parentRecord);
        setDescription("Computing individuals belonging to class");
        setMessage("Querying reasoner...");
     
        individuals.addAll(protegeReasoner.getIndividualsBelongingToClass(aClass));
        
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

