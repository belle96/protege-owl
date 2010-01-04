package edu.stanford.smi.protegex.owl.inference.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.util.DeleteAnonymousClassHook;
import edu.stanford.smi.protegex.owl.util.DeleteAnonymousClassUtil;

public class DeleteAnonymousClassHookTestCase extends AbstractJenaTestCase {
    
    private transient static final Logger log = Log.getLogger(DeleteAnonymousClassHookTestCase.class);
    
    private List<ClassAndRoot> deleted = new ArrayList<ClassAndRoot>();
    
    public void test01() {        
        DeleteAnonymousClassUtil.addDeleteAnonymousClassHook(owlModel, new MyHook());
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLObjectProperty p = owlModel.createOWLObjectProperty("p");
        OWLObjectProperty q = owlModel.createOWLObjectProperty("q");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        OWLNamedClass c = owlModel.createOWLNamedClass("C");
        
        OWLSomeValuesFrom r1 = owlModel.createOWLSomeValuesFrom(p, b);
        a.addEquivalentClass(r1);
        OWLSomeValuesFrom r2 = owlModel.createOWLSomeValuesFrom(p, c);
        OWLSomeValuesFrom r3 = owlModel.createOWLSomeValuesFrom(q, r2);
        
        deleted.clear();
        a.removeEquivalentClass(r1);
        assert(deleted.size() == 1);
        assert(deleted.get(0).getCls().equals(r1));
        assert(deleted.get(0).getRoot().equals(r1));
        
        a.addEquivalentClass(r3);
        deleted.clear();
        a.removeEquivalentClass(r3);
        assert(deleted.size() == 2);
        boolean inner = false;
        boolean outer = false;
        for (ClassAndRoot clsRoot : deleted) {
            assert(clsRoot.getRoot().equals(r3));
            assert(clsRoot.getCls().equals(r3) || clsRoot.getCls().equals(r2));
            if (clsRoot.getCls().equals(r3)) {
                outer = true;
            }
            else {
                inner = true;
            }
        }
        assert(inner && outer);
    }
    
    
    // The C&P case
    public void test02() {
        final Map<OWLAnonymousClass, String> deletedMap = new HashMap<OWLAnonymousClass, String>();
        DeleteAnonymousClassUtil.addDeleteAnonymousClassHook(owlModel, new DeleteAnonymousClassHook() {

            public void delete(OWLAnonymousClass root, OWLAnonymousClass cls) {
                if (!deletedMap.containsKey(root)) {
                    deletedMap.put(root, root.getBrowserText());
                }
            }
            
        });
        
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLObjectProperty p = owlModel.createOWLObjectProperty("p");
        OWLObjectProperty q = owlModel.createOWLObjectProperty("q");
        OWLNamedClass c = owlModel.createOWLNamedClass("C");
        OWLSomeValuesFrom r2 = owlModel.createOWLSomeValuesFrom(p, c);
        OWLSomeValuesFrom r3 = owlModel.createOWLSomeValuesFrom(q, r2);
        String expectedBrowserText = r3.getBrowserText();
        
        a.addEquivalentClass(r3);
        deletedMap.clear();
        a.removeEquivalentClass(r3);
        assert(deletedMap.size() == 1);
        for (Entry<OWLAnonymousClass, String> entry : deletedMap.entrySet()) {
            assert(r3.equals(entry.getKey()));
            assert(expectedBrowserText.equals(entry.getValue()));
        }
    }
    
    private class MyHook implements DeleteAnonymousClassHook {
        public void delete(OWLAnonymousClass root,
                           OWLAnonymousClass cls) {
            deleted.add(new ClassAndRoot(cls, root));
        }
        
    }
    
    private class ClassAndRoot {
        private OWLAnonymousClass cls;
        private OWLAnonymousClass root;
        
        public ClassAndRoot(OWLAnonymousClass cls, OWLAnonymousClass root) {
            super();
            this.cls = cls;
            this.root = root;
        }
        
        public OWLAnonymousClass getCls() {
            return cls;
        }
        
        public OWLAnonymousClass getRoot() {
            return root;
        }

    }
}
