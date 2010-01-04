package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collection;

public class InferredSuperclassesTestCase extends AbstractJenaTestCase {

    public void testSetComputedSubclasses() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        b.addInferredSuperclass(a);
        assertEquals(1, a.getInferredSubclasses().size());
        assertEquals(b, a.getInferredSubclasses().iterator().next());
        assertEquals(1, b.getInferredSuperclasses().size());
        assertEquals(a, b.getInferredSuperclasses().iterator().next());
    }


    public void testSetThingAsComputedSuperclass() {
        RDFSClass rootCls = owlModel.getOWLThingClass();
        OWLNamedClass cls = owlModel.createOWLNamedClass("A");
        cls.addInferredSuperclass(rootCls);
        Collection rootSubClasses = rootCls.getPropertyValues(owlModel.getProtegeInferredSubclassesProperty());
        assertEquals(1, rootSubClasses.size());
        assertEquals(cls, rootSubClasses.iterator().next());
        assertEquals(1, cls.getInferredSuperclasses().size());
        assertEquals(rootCls, cls.getInferredSuperclasses().iterator().next());
    }
}
