package edu.stanford.smi.protegex.owl.util.job;

import java.util.Iterator;

import edu.stanford.smi.protege.exception.ProtegeException;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.LocalizeUtils;
import edu.stanford.smi.protege.util.ProtegeJob;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;

/*
 * This is  a little twisted for a protege job - it does more processing
 * in execute than expected  and execute returns a different type than run.
 */

public class GetTripleStoreOfTripleJob extends ProtegeJob {
    private static final long serialVersionUID = -406814600433675121L;

    private RDFResource subject;
    private Slot slot;
    private Object object;
    
    public GetTripleStoreOfTripleJob(RDFResource subject, Slot slot, Object object) {
        super(subject.getOWLModel());
        this.subject = subject;
        this.slot = slot;
        this.object = object;
    }

    @Override
    public OWLModel getKnowledgeBase() {
        return (OWLModel) super.getKnowledgeBase();
    }
    /*
     * we had a problem with generics and rmi so we don't use them.
     */
    
    @Override
    public String run() throws ProtegeException {
        TripleStore tripleStore = getTripleStore();
        if (tripleStore != null) {
            return tripleStore.getName();
        }
        else {
            return null;
        }
    }
    
    private TripleStore getTripleStore() {
        if (object instanceof RDFSLiteral) {
            object = ((DefaultRDFSLiteral) object).getRawValue();
        }
        OWLModel owlModel = subject.getOWLModel();
        Iterator<TripleStore> it = owlModel.getTripleStoreModel().getTripleStores().iterator();
        while (it.hasNext()) {
            TripleStore ts = it.next();
            if (ts.getNarrowFrameStore().getValues(subject, slot, null, false).contains(object)) {
                return ts;
            }
        }
        return null;
    }
    
    @Override
    public TripleStore execute() throws ProtegeException {
        if  (!getKnowledgeBase().getProject().isMultiUserClient()) {
            return getTripleStore();
        }
        else {
            String tripleStoreName = (String) super.execute();
            for (TripleStore tripleStore : getKnowledgeBase().getTripleStoreModel().getTripleStores()) {
                if (tripleStore.getName().equals(tripleStoreName)) {
                    return tripleStore;
                }
            }
            return null;
        }
    }
    
    @Override
    public void localize(KnowledgeBase kb) {
        super.localize(kb);
        LocalizeUtils.localize(subject, kb);
        LocalizeUtils.localize(slot, kb);
        LocalizeUtils.localize(object, kb);
    }

}
