package edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner;

import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecordFactory;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.inference.util.TimeDifference;
import edu.stanford.smi.protegex.owl.model.OWLClass;

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

    private ProtegeReasoner protegeReasoner;

    private boolean satisfiable;


    public GetConceptSatisfiableTask(OWLClass aClass,
                                     ProtegeReasoner protegeReasoner) {
        super(protegeReasoner);
        this.aClass = aClass;
        this.protegeReasoner = protegeReasoner;
        this.satisfiable = true;
    }


    public int getTaskSize() {
        return 1;
    }


    public void run() throws ProtegeReasonerException {      

        ReasonerLogRecordFactory logRecordFactory = ReasonerLogRecordFactory.getInstance();
        ReasonerLogRecord parentRecord = logRecordFactory.createInformationMessageLogRecord("Checking consistency of " + aClass.getBrowserText(),
                null);
        postLogRecord(parentRecord);
        setDescription("Computing consistency");

        setMessage("Querying reasoner...");
        TimeDifference td = new TimeDifference();
        td.markStart();

        satisfiable = protegeReasoner.isSatisfiable(aClass);

        td.markEnd();
        
        postLogRecord(logRecordFactory.createInformationMessageLogRecord("Time to query reasoner = " + td, parentRecord));
  

        postLogRecord(logRecordFactory.createConceptConsistencyLogRecord(aClass, satisfiable, parentRecord));
        setProgress(1);
        setMessage("Finished");
        setTaskCompleted();
    }


    public boolean getResult() {
        return satisfiable;
    }
}

