package edu.stanford.smi.protegex.owl.util;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.util.DeletionHook;
import edu.stanford.smi.protege.util.DeletionHookUtil;
import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class DeleteAnonymousClassUtil {
    
    public static void addDeleteAnonymousClassHook(OWLModel model, 
                                                   final DeleteAnonymousClassHook hook) {
        DeletionHookUtil.addDeletionHook(model, new DeletionHook() {

            public void delete(Frame frame) {
                if (frame instanceof  OWLAnonymousClass) {
                    OWLAnonymousClass cls = (OWLAnonymousClass) frame;
                    OWLAnonymousClass root = cls.getExpressionRoot();
                    hook.delete(root, cls);
                }
            }
            
        });
    }

}
