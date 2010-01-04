package edu.stanford.smi.protegex.owl.util;

import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;

public interface DeleteAnonymousClassHook {

    void delete(OWLAnonymousClass root, OWLAnonymousClass cls);
    
}
