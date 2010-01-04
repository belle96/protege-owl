package edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecordFactory;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.inference.util.ReasonerUtil;
import edu.stanford.smi.protegex.owl.inference.util.TimeDifference;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;


public class UpdateInconsistentClassesTask extends AbstractReasonerTask {

    private ProtegeReasoner protegeReasoner;


    public UpdateInconsistentClassesTask(ProtegeReasoner protegeReasoner) {
        super(protegeReasoner);
        this.protegeReasoner = protegeReasoner;
    }


    public int getTaskSize() {
        // The size depends on the number
        // of named classes.

        return ReasonerUtil.getInstance().getNamedClses(protegeReasoner.getOWLModel()).size();
    }


    public void run() throws ProtegeReasonerException {

        OWLModel owlModel = protegeReasoner.getOWLModel();

        ReasonerLogRecord parentRecord = ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Check concept consistency",
                null);
        postLogRecord(parentRecord);

        setProgress(0);

        TimeDifference td = new TimeDifference();

        setDescription("Computing inconsistent concepts");

        // This ought to be changed so that only concepts in the TBox are queried

        setMessage("Querying reasoner for inconsistent concepts and updating Protege-OWL...");
        
        ReasonerLogRecord icParentRecord = null;
        
        td.markStart();
        
        boolean eventsEnabled = owlModel.setGenerateEventsEnabled(false);        

        try {
	        owlModel.beginTransaction("Compute and mark inconsistent classes");
	
	        // Get an iterator which we can use to
	        // traverse the query responses
	
	        OWLNamedClass owlNothing = owlModel.getOWLNothing();
	        
	        Collection allClses = ReasonerUtil.getInstance().getNamedClses(owlModel);
	        
	        for (Iterator iterator = allClses.iterator(); iterator.hasNext();) {
				OWLNamedClass curNamedCls = (OWLNamedClass) iterator.next();
				
			    doAbortCheck();
			    
			    boolean isConsistent = protegeReasoner.isSatisfiable(curNamedCls);
		    			    
                if (isConsistent) {
                    curNamedCls.setClassificationStatus(OWLNames.CLASSIFICATION_STATUS_CONSISTENT_AND_UNCHANGED);
                    curNamedCls.removeInferredSuperclass(owlNothing);
                }
                else {
                    if (icParentRecord == null) {
                        icParentRecord = ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Inconsistent concepts",
                                parentRecord);
                        postLogRecord(icParentRecord);
                    }

                    postLogRecord(ReasonerLogRecordFactory.getInstance().createConceptConsistencyLogRecord(curNamedCls,
                            false, icParentRecord));
                    curNamedCls.setClassificationStatus(OWLNames.CLASSIFICATION_STATUS_INCONSISTENT);
                    
                    Collection inferredSuperclasses = curNamedCls.getInferredSuperclasses();
                    
                    if (!inferredSuperclasses.contains(owlNothing)) {
                    	curNamedCls.addInferredSuperclass(owlNothing);
                    }
                }
			
                setProgress(getProgress() + 1);
			}
	         
	
	        td.markEnd();
	
	        postLogRecord(ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Time to update Protege-OWL = " + td,
	                parentRecord));
	
	        setTaskCompleted();
	        owlModel.commitTransaction();
        }        
        catch (ProtegeReasonerException e) {
        	owlModel.rollbackTransaction();
        	throw e;
        }
        catch (Exception e) {        	
        	Log.getLogger().log(Level.WARNING, "Exception in transaction. Rollback. Exception: " + e.getMessage(), e);
        	owlModel.rollbackTransaction();
        	RuntimeException re = new RuntimeException();
        	re.initCause(e);
        	throw re;
		}
        
        owlModel.setGenerateEventsEnabled(eventsEnabled);
    }


}

