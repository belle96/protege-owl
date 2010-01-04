package edu.stanford.smi.protegex.owl.inference.protegeowl.log;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 23, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ReasonerLogRecordFactory {

    private static ReasonerLogRecordFactory instance;


    protected ReasonerLogRecordFactory() {

    }


    public synchronized static ReasonerLogRecordFactory getInstance() {
        if (instance == null) {
            instance = new ReasonerLogRecordFactory();
        }

        return instance;
    }


    public ReasonerLogRecord createInformationMessageLogRecord(String message,
                                                               ReasonerLogRecord parent) {
        return new InformationMessageLogRecord(null, message, parent);
    }
 

    public ReasonerLogRecord createWarningMessageLogRecord(RDFResource cause,
                                                           String message,
                                                           ReasonerLogRecord parent) {
        return new WarningMessageLogRecord(cause, message, parent);
    }


    public ReasonerLogRecord createErrorMessageLogRecord(String message,
                                                         ReasonerLogRecord parent) {
        return new ErrorMessageLogRecord(null, message, parent);
    }


    public ReasonerLogRecord createConceptConsistencyLogRecord(RDFSClass aClass, boolean consistent, ReasonerLogRecord parent) {
        return new DefaultConceptConsistencyLogRecord(aClass, consistent, parent);
    }


    public ReasonerLogRecord createOWLInstanceLogRecord(RDFResource instance, ReasonerLogRecord parent) {
        return new OWLInstanceLogRecord(instance, parent);
    }


    public ReasonerLogRecord createOWLPropertyLogRecord(RDFProperty prop,  ReasonerLogRecord parent) {
    	return new OWLPropertyLogRecord(prop, parent);
    }

    
    
    public ReasonerLogRecord createDIGReasonerExceptionLogRecord(DIGReasonerException ex, ReasonerLogRecord parent) {
        return new DIGErrorExceptionLogRecord(ex, parent);
    }
    
    public ReasonerLogRecord createReasonerExceptionLogRecord(ProtegeReasonerException ex, ReasonerLogRecord parent) {
        return new ReasonerErrorExceptionLogRecord(ex, parent);
    }

}

