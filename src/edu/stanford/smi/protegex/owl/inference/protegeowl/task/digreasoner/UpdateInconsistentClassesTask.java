package edu.stanford.smi.protegex.owl.inference.protegeowl.task.digreasoner;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.dig.translator.DIGQueryResponse;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecordFactory;
import edu.stanford.smi.protegex.owl.inference.util.ReasonerUtil;
import edu.stanford.smi.protegex.owl.inference.util.TimeDifference;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import org.w3c.dom.Document;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jul 23, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class UpdateInconsistentClassesTask extends AbstractReasonerTask {


    private ProtegeOWLReasoner protegeOWLReasoner;


    public UpdateInconsistentClassesTask(ProtegeOWLReasoner protegeOWLReasoner) {
        super(protegeOWLReasoner);
        this.protegeOWLReasoner = protegeOWLReasoner;
    }


    public int getTaskSize() {
        // The size depends on the number
        // of named classes.

        return ReasonerUtil.getInstance().getNamedClses(protegeOWLReasoner.getKnowledgeBase()).size();
    }


    public void run() throws DIGReasonerException {

        OWLModel kb = protegeOWLReasoner.getKnowledgeBase();

        ReasonerLogRecord parentRecord = ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Check concept consistency",
                null);
        postLogRecord(parentRecord);

        setProgress(0);

        TimeDifference td = new TimeDifference();

        setDescription("Computing inconsistent concepts");

        Document asksDoc;

        setMessage("Building reasoner query...");

        td.markStart();

        // Generate a satisfiability query for every
        // named class in the knowledgebase
        asksDoc = getTranslator().createAsksDocument(protegeOWLReasoner.getReasonerKnowledgeBaseURI());

        Collection namedClses = ReasonerUtil.getInstance().getNamedClses(kb);

        // This ought to be changed so that only concepts in the TBox are queried


        Iterator namedClsesIt = namedClses.iterator();

        while (namedClsesIt.hasNext()) {
            final OWLNamedClass curNamedCls = (OWLNamedClass) namedClsesIt.next();

            getTranslator().createSatisfiableQuery(asksDoc, curNamedCls.getName(), curNamedCls);

            doAbortCheck();
        }

        td.markEnd();

        postLogRecord(ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Time to build query = " + td,
                parentRecord));

        td.markStart();

        setMessage("Querying reasoner for inconsistent concepts...");

        // Send the satisfiability request to the reasoner

        setProgressIndeterminate(true);

        Document responseDoc = protegeOWLReasoner.getDIGReasoner().performRequest(asksDoc);

        setProgressIndeterminate(false);

        doAbortCheck();

        td.markEnd();

        postLogRecord(ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Time to send and receive from reasoner = " + td,
                parentRecord));

        // Update Protege-OWL

        setMessage("Updating Protege-OWL...");

        td.markStart();

        boolean eventsEnabled = kb.setGenerateEventsEnabled(false);        

        try {
	        kb.beginTransaction("Compute and mark inconsistent classes");
	
	        OWLNamedClass owlNothing = kb.getOWLNothing();
	        
	        // Get an iterator which we can use to
	        // traverse the query responses
	        Iterator responseIt = getTranslator().getDIGQueryResponseIterator(kb, responseDoc);
		
	        DIGQueryResponse response;
	
	        String queryID;
	
	        ReasonerLogRecord icParentRecord = null;
	
	        while (responseIt.hasNext()) {
	            doAbortCheck();
	
	            response = (DIGQueryResponse) responseIt.next();
	
	            queryID = response.getID();
	
	            final OWLNamedClass curNamedCls = kb.getOWLNamedClass(queryID);
	
	            if (curNamedCls != null) {
	                if (response.getBoolean() == true) {
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
	                            false,
	                            icParentRecord));
	                    Collection inferredSuperclasses = curNamedCls.getInferredSuperclasses();
	                    
	                    if (!inferredSuperclasses.contains(owlNothing)) {
	                    	curNamedCls.addInferredSuperclass(owlNothing);
	                    }
	                    
	                    curNamedCls.setClassificationStatus(OWLNames.CLASSIFICATION_STATUS_INCONSISTENT);                    
	                    
	                }
	            }
	
	            setProgress(getProgress() + 1);
	        }
	
	        td.markEnd();
	
	        postLogRecord(ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Time to update Protege-OWL = " + td,
	                parentRecord));
	
	        setTaskCompleted();
	        kb.commitTransaction();
        }        
        catch (DIGReasonerException e) {
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
    }


}

