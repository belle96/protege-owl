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
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import org.w3c.dom.Document;

import java.util.Collection;
import java.util.Iterator;

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

    private ProtegeOWLReasoner protegeOWLReasoner;


    public UpdateInferredHierarchyTask(ProtegeOWLReasoner protegeOWLReasoner) {
        super(protegeOWLReasoner);
        this.protegeOWLReasoner = protegeOWLReasoner;
    }


    public int getTaskSize() {
        return ReasonerUtil.getInstance().getNamedClses(protegeOWLReasoner.getKnowledgeBase()).size();
    }


    public void run() throws DIGReasonerException {

        OWLModel kb = protegeOWLReasoner.getKnowledgeBase();

        ReasonerLogRecord parentRecord = ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Compute inferred hierarchy",
                null);

        doAbortCheck();

        postLogRecord(parentRecord);

        setProgress(0);

        setDescription("Computing inferred hierarchy");

        setMessage("Building inferred hierarchy reasoner query...");

        TimeDifference td = new TimeDifference();
        td.markStart();

        Document asksDoc = getTranslator().createAsksDocument(protegeOWLReasoner.getReasonerKnowledgeBaseURI());

        Collection namedClses = ReasonerUtil.getInstance().getNamedClses(kb);

        Iterator namedClsesIt = namedClses.iterator();


        while (namedClsesIt.hasNext()) {
            final OWLNamedClass curNamedCls;

            curNamedCls = (OWLNamedClass) namedClsesIt.next();

            getTranslator().createDirectSuperConceptsQuery(asksDoc, curNamedCls.getName(), curNamedCls);
        }


        td.markEnd();

        postLogRecord(ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Time to build query = " + td,
                parentRecord));

        doAbortCheck();

        setMessage("Querying reasoner...");

        td.markStart();

        Document responseDoc = protegeOWLReasoner.getDIGReasoner().performRequest(asksDoc);

        td.markEnd();

        postLogRecord(ReasonerLogRecordFactory.getInstance().createInformationMessageLogRecord("Time to query reasoner = " + td,
                parentRecord));

        doAbortCheck();

        setMessage("Updating Protege-OWL...");

        td.markStart();
        
        boolean eventsEnabled = kb.setGenerateEventsEnabled(false);
        try {
	        kb.beginTransaction("Compute and update inferred class hierarchy");
	
	        for (Iterator it = getTranslator().getDIGQueryResponseIterator(kb, responseDoc); it.hasNext();) {
	
	            doAbortCheck();
	
	            final DIGQueryResponse response = (DIGQueryResponse) it.next();
	
	            final String curQueryID = response.getID();
	
	            final OWLNamedClass curNamedCls = kb.getOWLNamedClass(curQueryID);
	
	            if (curNamedCls != null) {
	                if (curNamedCls.isConsistent()) {
	
	                    final Collection infSuperClses = response.getConcepts();
	
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
