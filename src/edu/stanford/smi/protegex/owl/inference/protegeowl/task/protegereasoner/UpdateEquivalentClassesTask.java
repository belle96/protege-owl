package edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner;

import java.util.Collection;
import java.util.Iterator;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecordFactory;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.inference.util.ReasonerUtil;
import edu.stanford.smi.protegex.owl.inference.util.TimeDifference;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jul 23, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class UpdateEquivalentClassesTask extends AbstractReasonerTask {

    private ProtegeReasoner protegeReasoner;


    public UpdateEquivalentClassesTask(ProtegeReasoner protegeReasoner) {
        super(protegeReasoner);
        this.protegeReasoner = protegeReasoner;
    }


    public int getTaskSize() {
        return ReasonerUtil.getInstance().getNamedClses(protegeReasoner.getOWLModel()).size();
    }


    public void run() throws ProtegeReasonerException {
        OWLModel kb = protegeReasoner.getOWLModel();

        ReasonerLogRecord parentRecord = ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Compute equivalent classes",
                null);

        doAbortCheck();

        postLogRecord(parentRecord);

        TimeDifference td = new TimeDifference();

        setProgress(0);

        setDescription("Computing equivalent classes");

        // Send the query and get the response

        setMessage("Querying reasoner and updating Protege-OWL...");

        td.markStart();
                
        boolean eventsEnabled = kb.setGenerateEventsEnabled(false);
        try {
	        kb.beginTransaction("Compute and update equivalent classes");

	        Collection allClses = ReasonerUtil.getInstance().getNamedClses(kb);
	        
	        for (Iterator iterator = allClses.iterator(); iterator.hasNext();) {
				OWLNamedClass curNamedCls = (OWLNamedClass) iterator.next();
				
			    doAbortCheck();

			    if (curNamedCls.isConsistent()) {

			    	Collection equivalentClses = protegeReasoner.getEquivalentClasses(curNamedCls);

			    	for (Iterator iterator2 = equivalentClses.iterator(); iterator2.hasNext();) {
			    		OWLClass superCls = (OWLClass) iterator2.next();

			    		if (superCls.isVisible() && superCls instanceof OWLNamedClass) {

			    			OWLNamedClass curSuperCls = (OWLNamedClass) superCls;

			    			if (curSuperCls.equals(curNamedCls) == false) {
			    				if (curNamedCls.getInferredSuperclasses().contains(curSuperCls) == false) {
			    					curNamedCls.addInferredSuperclass(curSuperCls);
			    				}

			    				if (curSuperCls.getInferredSuperclasses().contains(curNamedCls) == false) {
			    					curSuperCls.addInferredSuperclass(curNamedCls);
			    				}
			    				curNamedCls.setClassificationStatus(OWLNames.CLASSIFICATION_STATUS_CONSISTENT_AND_CHANGED);
			    			}
			    		}
			    	}
			    }

                setProgress(getProgress() + 1);   
			}
            
	
	        kb.commitTransaction();
        }
        catch (ProtegeReasonerException e) {
        	kb.rollbackTransaction();
        	throw e;
        }
        catch (Exception e) {
        	kb.rollbackTransaction();
        	Log.getLogger().warning("Exception in transaction. Rollback. Exception: " + e.getMessage());
        	RuntimeException re = new RuntimeException();
        	re.initCause(e);
        	throw re;
		}

        
        kb.setGenerateEventsEnabled(eventsEnabled);

        td.markEnd();
        postLogRecord(ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Time to update Protege-OWL = " + td,
                parentRecord));
        setTaskCompleted();
    }

}

