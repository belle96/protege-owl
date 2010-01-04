/**
 * 
 */
package edu.stanford.smi.protegex.owl.util.job;

import java.util.Collection;

import edu.stanford.smi.protege.exception.ProtegeException;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.framestore.MergingNarrowFrameStore;
import edu.stanford.smi.protege.model.framestore.NarrowFrameStore;
import edu.stanford.smi.protege.util.LocalizeUtils;
import edu.stanford.smi.protege.util.ProtegeJob;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStoreModel;

public class GetIsIncludedJob extends ProtegeJob {
    private RDFResource resource;
    
    /*
     * I had some mysterious trouble with generics and rmi which is why the 
     * Protege Job does not work that way.
     */
    
    public GetIsIncludedJob(RDFResource resource) {
        super(resource.getOWLModel());
        this.resource = resource;
    }
    
    private static final long serialVersionUID = -8370182309400148050L;

    @Override
    public Boolean run() throws ProtegeException {
        OWLModel owlModel = (OWLModel) getKnowledgeBase();
        TripleStoreModel tripleStoreModel = owlModel.getTripleStoreModel();
        NarrowFrameStore activeNfs = tripleStoreModel.getActiveTripleStore().getNarrowFrameStore();
        MergingNarrowFrameStore mnfs = MergingNarrowFrameStore.get(owlModel);
        for (NarrowFrameStore nfs : mnfs.getAvailableFrameStores()) {
            if (nfs.equals(activeNfs)) {
                continue;
            }
            Collection names = nfs.getValues(resource, owlModel.getSystemFrames().getNameSlot(), null, false);
            if (names != null && names.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public Boolean execute() {
        return (Boolean) super.execute();
    }
    
    @Override
    public void localize(KnowledgeBase kb) {
        super.localize(kb);
        LocalizeUtils.localize(resource, kb);
    }
}