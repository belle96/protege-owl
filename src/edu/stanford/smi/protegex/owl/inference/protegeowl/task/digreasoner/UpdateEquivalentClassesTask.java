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
public class UpdateEquivalentClassesTask extends AbstractReasonerTask {

    private ProtegeOWLReasoner protegeOWLReasoner;


    public UpdateEquivalentClassesTask(ProtegeOWLReasoner protegeOWLReasoner) {
        super(protegeOWLReasoner);
        this.protegeOWLReasoner = protegeOWLReasoner;
    }


    public int getTaskSize() {
        return ReasonerUtil.getInstance().getNamedClses(protegeOWLReasoner.getKnowledgeBase()).size();
    }


    public void run() throws DIGReasonerException {
        OWLModel kb = protegeOWLReasoner.getKnowledgeBase();

        ReasonerLogRecord parentRecord = ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Compute equivalent classes",
                null);

        doAbortCheck();

        postLogRecord(parentRecord);

        TimeDifference td = new TimeDifference();

        setProgress(0);

        setDescription("Computing equivalent classes");

        // Build the query
        setMessage("Building equivalent classes reasoner query...");

        td.markStart();

        Document asksDoc = getTranslator().createAsksDocument(protegeOWLReasoner.getReasonerKnowledgeBaseURI());

        Collection namedClses = ReasonerUtil.getInstance().getNamedClses(kb);

        Iterator namedClsesIt = namedClses.iterator();

        while (namedClsesIt.hasNext()) {
            final OWLNamedClass curNamedCls = (OWLNamedClass) namedClsesIt.next();

            getTranslator().createEquivalentConceptsQuery(asksDoc, curNamedCls.getName(), curNamedCls);
        }

        td.markEnd();

        postLogRecord(ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Time to build query = " + td,
                parentRecord));

        doAbortCheck();

        // Send the query and get the response

        setMessage("Querying reasoner...");

        td.markStart();

        Document responseDoc = protegeOWLReasoner.getDIGReasoner().performRequest(asksDoc);

        td.markEnd();

        postLogRecord(ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Time to query reasoner = " + td,
                parentRecord));

        doAbortCheck();

        // Update Protge-OWL

        setMessage("Updating Protege-OWL...");

        td.markStart();
        
        boolean eventsEnabled = kb.setGenerateEventsEnabled(false);

        try {
	        kb.beginTransaction("Compute and update equivalent classes");
	
	        Iterator responseIt = getTranslator().getDIGQueryResponseIterator(kb, responseDoc);
	
	        while (responseIt.hasNext()) {
	            doAbortCheck();
	
	            final DIGQueryResponse response = (DIGQueryResponse) responseIt.next();
	            final String curQueryID = response.getID();
	            final OWLNamedClass curNamedCls = kb.getOWLNamedClass(curQueryID);
	
	            if (curNamedCls != null) {
	                if (curNamedCls.isConsistent()) {
	                    Iterator clsesIt;
	
	                    clsesIt = response.getConcepts().iterator();
	
	                    while (clsesIt.hasNext()) {
	                        final OWLNamedClass curSuperCls = (OWLNamedClass) clsesIt.next();
	
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

        td.markEnd();
        postLogRecord(ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Time to update Protege-OWL = " + td,
                parentRecord));
        setTaskCompleted();
    }

}

