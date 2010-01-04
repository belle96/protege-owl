package edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecordFactory;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 17, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class GetIndividualInferredTypesTask extends AbstractReasonerTask implements CollectionResultReasonerTask {

    private OWLIndividual individual;

    private ProtegeReasoner protegeReasoner;

    private HashSet types;


    public GetIndividualInferredTypesTask(OWLIndividual individual,
                                          ProtegeReasoner protegeReasoner) {
        super(protegeReasoner);
        this.individual = individual;
        this.protegeReasoner = protegeReasoner;

        types = new HashSet();
    }


    public int getTaskSize() {
        return 1;
    }


    public void run()
            throws ProtegeReasonerException {
        
        ReasonerLogRecordFactory logRecordFactory = ReasonerLogRecordFactory.getInstance();

        setDescription("Getting types for individual");
        setProgress(0);
        setMessage("Building reasoner query...");

        setMessage("Querying reasoner...");

        ReasonerLogRecord parentRecord = logRecordFactory.createInformationMessageLogRecord("Inferred types for: " + individual.getBrowserText(), null);
        postLogRecord(parentRecord);
  
        types.addAll(protegeReasoner.getIndividualTypes(individual));

        Iterator typesIt = types.iterator();

        while (typesIt.hasNext()) {
            final RDFSClass curClass = (RDFSClass) typesIt.next();

            postLogRecord(logRecordFactory.createOWLInstanceLogRecord(curClass, parentRecord));
        }

        setMessage("Finished");

        setTaskCompleted();
    }


    public Collection getResult() {
        return types;
    }
}

