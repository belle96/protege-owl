package edu.stanford.smi.protegex.owl.testing.sanity.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.testing.sanity.SubpropertyRangeMustNarrowTest;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collections;

public class SubPropertyRangeMustNarrowTestTestCase extends AbstractJenaTestCase {


    public void testSimpleNarrowing() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty superSlot = owlModel.createOWLObjectProperty("superSlot");
        OWLObjectProperty subSlot = owlModel.createOWLObjectProperty("subSlot");
        subSlot.addSuperproperty(superSlot);
        subSlot.addUnionRangeClass(cls);
        assertNull(SubpropertyRangeMustNarrowTest.fails(superSlot));
        assertNull(SubpropertyRangeMustNarrowTest.fails(subSlot));
    }


    public void testSimpleWidening() {
        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperCls");
        OWLNamedClass subCls = owlModel.createOWLNamedSubclass("SubCls", superCls);
        OWLObjectProperty superSlot = owlModel.createOWLObjectProperty("superSlot");
        superSlot.addUnionRangeClass(subCls);
        OWLObjectProperty subSlot = owlModel.createOWLObjectProperty("subSlot");
        subSlot.addSuperproperty(superSlot);
        subSlot.setUnionRangeClasses(Collections.singleton(superCls));
        assertSize(1, subSlot.getUnionRangeClasses());

        assertNull(SubpropertyRangeMustNarrowTest.fails(superSlot));
        assertEquals(superSlot, SubpropertyRangeMustNarrowTest.fails(subSlot));
    }


    public void testUnionWidening() {

        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperCls");
        OWLNamedClass subCls = owlModel.createOWLNamedSubclass("SubCls", superCls);
        OWLObjectProperty superSlot = owlModel.createOWLObjectProperty("superSlot");
        superSlot.addUnionRangeClass(subCls);
        OWLObjectProperty subSlot = owlModel.createOWLObjectProperty("subSlot");
        subSlot.addSuperproperty(superSlot);
        subSlot.setUnionRangeClasses(Collections.singleton(superCls));
        subSlot.addUnionRangeClass(subCls);

        assertNull(SubpropertyRangeMustNarrowTest.fails(superSlot));
        assertEquals(superSlot, SubpropertyRangeMustNarrowTest.fails(subSlot));
    }


    public void testOk() {

        OWLNamedClass topCls = owlModel.createOWLNamedClass("Top");
        OWLNamedClass middleCls = owlModel.createOWLNamedSubclass("Middle", topCls);
        OWLNamedClass leafCls = owlModel.createOWLNamedSubclass("Leaf", middleCls);

        OWLObjectProperty topSlot = owlModel.createOWLObjectProperty("top");
        topSlot.setUnionRangeClasses(Collections.singleton(topCls));
        OWLObjectProperty middleSlot = owlModel.createOWLObjectProperty("middle");
        middleSlot.addSuperproperty(topSlot);
        middleSlot.setUnionRangeClasses(Collections.singleton(middleCls));
        OWLObjectProperty leafSlot = owlModel.createOWLObjectProperty("leafSlot");
        leafSlot.addSuperproperty(middleSlot);
        leafSlot.setUnionRangeClasses(Collections.singleton(leafCls));

        assertNull(SubpropertyRangeMustNarrowTest.fails(topSlot));
        assertNull(SubpropertyRangeMustNarrowTest.fails(middleSlot));
        assertNull(SubpropertyRangeMustNarrowTest.fails(leafSlot));
    }
}
