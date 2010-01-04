package edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jul 22, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class UpdateInferredHierarchyTask extends AbstractReasonerTask {
    private static transient final Logger log = Log.getLogger(UpdateInferredHierarchyTask.class);

    private ProtegeReasoner protegeReasoner;


    public UpdateInferredHierarchyTask(ProtegeReasoner protegeReasoner) {
        super(protegeReasoner);
        this.protegeReasoner = protegeReasoner;
    }


    public int getTaskSize() {
        return ReasonerUtil.getInstance().getNamedClses(protegeReasoner.getOWLModel()).size();
    }


    public void run() throws ProtegeReasonerException {

        OWLModel kb = protegeReasoner.getOWLModel();

        ReasonerLogRecord parentRecord = ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Compute inferred hierarchy",
                null);

        doAbortCheck();

        postLogRecord(parentRecord);

        setProgress(0);

        setDescription("Computing inferred hierarchy");
        
        setMessage("Querying reasoner and updating Protege-OWL...");
        
        TimeDifference td = new TimeDifference();
        td.markStart();
        
        boolean eventsEnabled = kb.setGenerateEventsEnabled(false);
        try {
	        kb.beginTransaction("Compute and update inferred class hierarchy");
	
	        Collection allClses = ReasonerUtil.getInstance().getNamedClses(kb);
	        
	        for (Iterator iterator = allClses.iterator(); iterator.hasNext();) {
	            doAbortCheck();
	            
	            OWLNamedClass curNamedCls = (OWLNamedClass) iterator.next();
	  	
	            //TT- maybe this should be removed	            
	               if (curNamedCls.isConsistent()) {
	            	   
	            	    long t0 = System.currentTimeMillis();
	                    Collection infSuperClses = protegeReasoner.getSuperclasses(curNamedCls);
                            if (log.isLoggable(Level.FINE)) {
                                log.fine("Query to get inf superclses for  " + curNamedCls.getName() + ": " + (System.currentTimeMillis() - t0));
                            }
	                    
	                    for (Iterator clsesIt = infSuperClses.iterator(); clsesIt.hasNext();) {
	                        final RDFSClass curSuperClass = (RDFSClass) clsesIt.next();
	                        // We don't want to assign invisible super classes!
	                        if (curSuperClass.isVisible() == true) {
	                            curNamedCls.addInferredSuperclass(curSuperClass);
	                        }
	                    }
	
	                    final Collection namedDirSuperCles = curNamedCls.getNamedSuperclasses();
	
	                    if (namedDirSuperCles.containsAll(infSuperClses) == false ||
	                            infSuperClses.containsAll(namedDirSuperCles) == false) {
	                        curNamedCls.setClassificationStatus(OWLNames.CLASSIFICATION_STATUS_CONSISTENT_AND_CHANGED);
	                    }
	                    else {
	                        curNamedCls.setClassificationStatus(OWLNames.CLASSIFICATION_STATUS_CONSISTENT_AND_UNCHANGED);
	                    }
	                }
	                else {
	                    for (Iterator namedDirSuperClsIt = curNamedCls.getNamedSuperclasses().iterator(); namedDirSuperClsIt.hasNext();) {
	                        curNamedCls.addInferredSuperclass((RDFSClass) namedDirSuperClsIt.next());
	                    }
	                }
          
	               
	            setProgress(getProgress() + 1);
	        }
	        kb.commitTransaction();
        }
        catch (ProtegeReasonerException e) {
        	setTaskFailed();
        	kb.rollbackTransaction();
        	throw e;
        }
        catch (Exception e) {
        	setTaskFailed();
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
