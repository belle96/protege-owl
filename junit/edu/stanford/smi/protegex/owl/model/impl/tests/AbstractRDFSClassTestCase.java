package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AbstractRDFSClassTestCase extends AbstractJenaTestCase {

    public void testSuperclasses() {
        RDFSNamedClass superclass = owlModel.createRDFSNamedClass("superclass");
        RDFSNamedClass middleclass = owlModel.createRDFSNamedSubclass("middleclass", superclass);
        RDFSNamedClass subclass = owlModel.createRDFSNamedSubclass("subclass", middleclass);

        assertEquals(1, superclass.getSubclassCount());
        assertEquals(1, middleclass.getSubclassCount());
        assertEquals(0, subclass.getSubclassCount());
        assertSize(1, superclass.getSubclasses(false));
        assertSize(2, superclass.getSubclasses(true));
        assertTrue(subclass.isSubclassOf(middleclass));
        assertTrue(middleclass.isSubclassOf(superclass));

        assertEquals(1, superclass.getSuperclassCount());
        assertEquals(1, middleclass.getSuperclassCount());
        assertEquals(1, subclass.getSuperclassCount());
        assertSize(1, superclass.getSuperclasses(false));
        assertSize(1, middleclass.getSuperclasses(false));
        assertSize(2, middleclass.getSuperclasses(true));
        assertSize(3, subclass.getSuperclasses(true));
        assertContains(owlThing, subclass.getSuperclasses(true));
        assertContains(superclass, subclass.getSuperclasses(true));
        assertContains(middleclass, subclass.getSuperclasses(true));
    }


    public void testSuperclassesOfRDFProperty() {
        RDFSClass rdfPropertyClass = owlModel.getRDFPropertyClass();
        Collection ss = rdfPropertyClass.getSuperclasses(false);
        assertSize(1, ss);
        assertContains(owlModel.getOWLThingClass(), ss);
    }


    public void testInferredInstances() {
        OWLNamedClass superClass = owlModel.createOWLNamedClass("Super");
        OWLNamedClass middleClass = owlModel.createOWLNamedSubclass("Middle", superClass);
        OWLNamedClass subClass = owlModel.createOWLNamedSubclass("Sub", middleClass);
        RDFIndividual indi = (RDFIndividual) superClass.createInstance("individual");
        indi.setInferredTypes(Collections.singleton(middleClass));
        assertSize(1, indi.getInferredTypes());
        assertContains(middleClass, indi.getInferredTypes());
        assertSize(0, superClass.getInferredInstances(false));
        assertSize(1, superClass.getInferredInstances(true));
        assertSize(1, middleClass.getInferredInstances(false));
        assertSize(1, middleClass.getInferredInstances(true));
        assertSize(0, subClass.getInferredInstances(false));
        assertSize(0, subClass.getInferredInstances(true));
    }


    public void testUnionDomainOnlyReturnsRDFProperties() {
        Collection properties = owlModel.getOWLThingClass().getUnionDomainProperties();
        for (Iterator it = properties.iterator(); it.hasNext();) {
            Object o = it.next();
            assertTrue(o instanceof RDFProperty);
        }
    }


    public void testUnionRangeClassNoRestriction() {
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        RDFProperty property = owlModel.createRDFProperty("property");
        property.setDomain(cls);
        assertSize(0, cls.getUnionRangeClasses(property));
        property.setRange(cls);
        assertSize(1, cls.getUnionRangeClasses(property));
        assertContains(cls, cls.getUnionRangeClasses(property));
    }


    public void testUnionRangeClassDirectRestriction() {
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        RDFProperty property = owlModel.createRDFProperty("property");
        property.setDomain(cls);
        assertSize(0, cls.getUnionRangeClasses(property));
        cls.addSuperclass(owlModel.createOWLAllValuesFrom(property, cls));
        assertSize(1, cls.getUnionRangeClasses(property));
        assertContains(cls, cls.getUnionRangeClasses(property));
    }


    public void testUnionRangeClassOverloadedRestriction() {
        RDFSNamedClass superclass = owlModel.createRDFSNamedClass("Superclass");
        RDFSNamedClass subclass = owlModel.createRDFSNamedSubclass("Subclass", superclass);
        RDFProperty property = owlModel.createRDFProperty("property");
        property.setDomain(superclass);
        assertSize(0, superclass.getUnionRangeClasses(property));
        assertSize(0, subclass.getUnionRangeClasses(property));
        superclass.addSuperclass(owlModel.createOWLAllValuesFrom(property, superclass));
        assertSize(1, superclass.getUnionRangeClasses(property));
        assertContains(superclass, superclass.getUnionRangeClasses(property));
        assertSize(1, subclass.getUnionRangeClasses(property));
        assertContains(superclass, subclass.getUnionRangeClasses(property));

        subclass.addSuperclass(owlModel.createOWLAllValuesFrom(property, subclass));
        assertSize(1, superclass.getUnionRangeClasses(property));
        assertContains(superclass, superclass.getUnionRangeClasses(property));
        assertSize(1, subclass.getUnionRangeClasses(property));
        assertContains(subclass, subclass.getUnionRangeClasses(property));
    }
}
