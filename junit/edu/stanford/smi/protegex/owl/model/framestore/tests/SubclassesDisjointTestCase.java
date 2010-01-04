package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collections;

public class SubclassesDisjointTestCase extends AbstractJenaTestCase {

    public void testCreateSubclassesOfUnflaggedCls() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Parent");
        OWLNamedClass subClsA = owlModel.createOWLNamedSubclass("A", cls);
        OWLNamedClass subClsB = owlModel.createOWLNamedSubclass("B", cls);
        assertSize(0, subClsA.getDisjointClasses());
        assertSize(0, subClsB.getDisjointClasses());
    }


    public void testCreateSubclassesOfFlaggedCls() throws Exception {
        loadRemoteOntology("import-protege.owl");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Parent");
        cls.setSubclassesDisjoint(true);
        OWLNamedClass subClsA = owlModel.createOWLNamedSubclass("A", cls);
        OWLNamedClass subClsB = owlModel.createOWLNamedSubclass("B", cls);
        assertSize(1, subClsA.getDisjointClasses());
        assertEquals(subClsB, subClsA.getDisjointClasses().iterator().next());
        assertSize(1, subClsB.getDisjointClasses());
        assertEquals(subClsA, subClsB.getDisjointClasses().iterator().next());
    }


    public void testCreateSubclassesOfFlaggedCls2() throws Exception {
        loadRemoteOntology("import-protege.owl");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Parent");
        cls.setSubclassesDisjoint(true);
        OWLNamedClass subClsA = (OWLNamedClass) owlModel.createCls("A", Collections.singleton(cls));
        OWLNamedClass subClsB = (OWLNamedClass) owlModel.createCls("B", Collections.singleton(cls));
        assertSize(1, subClsA.getDisjointClasses());
        assertEquals(subClsB, subClsA.getDisjointClasses().iterator().next());
        assertSize(1, subClsB.getDisjointClasses());
        assertEquals(subClsA, subClsB.getDisjointClasses().iterator().next());
    }


    public void testCreateSubclassesAndFlagCls() throws Exception {
        loadRemoteOntology("import-protege.owl");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Parent");
        OWLNamedClass subClsA = owlModel.createOWLNamedSubclass("A", cls);
        OWLNamedClass subClsB = owlModel.createOWLNamedSubclass("B", cls);
        OWLNamedClass subClsC = owlModel.createOWLNamedSubclass("C", cls);
        cls.setSubclassesDisjoint(true);
        assertSize(2, subClsA.getDisjointClasses());
        assertSize(2, subClsB.getDisjointClasses());
        assertSize(2, subClsC.getDisjointClasses());
    }
}
