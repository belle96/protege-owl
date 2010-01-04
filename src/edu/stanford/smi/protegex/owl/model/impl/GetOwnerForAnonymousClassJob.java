package edu.stanford.smi.protegex.owl.model.impl;

import java.util.Collection;

import edu.stanford.smi.protege.exception.ProtegeException;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.util.LocalizeUtils;
import edu.stanford.smi.protege.util.ProtegeJob;
import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;


public class GetOwnerForAnonymousClassJob extends ProtegeJob {

    private OWLAnonymousClass anonClass;
    
    public GetOwnerForAnonymousClassJob(KnowledgeBase kb, OWLAnonymousClass anonClass) {
        super(kb);
        this.anonClass = anonClass;
    }

    @Override
    public Object run() throws ProtegeException {
        Collection subclasses = anonClass.getSubclasses(false);
        if (subclasses.isEmpty()) {
            OWLAnonymousClass root = anonClass.getExpressionRoot();
            if (this.equals(root)) {
                return null;
            }
            else {
                return root.getOwner();
            }
        }
        else {
            return (OWLNamedClass) subclasses.iterator().next();
        }
    }
    
    @Override
    public void localize(KnowledgeBase kb) {     
        super.localize(kb);
        LocalizeUtils.localize(anonClass, kb);
    }

}
