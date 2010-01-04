package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Arrays;
import java.util.List;

/**
 * Test the automated delete of anonymous classes.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeleteClassTestCase extends AbstractJenaTestCase {

    /**
     * owl:Thing
     * SuperCls
     * SubCls   (< ? slot OtherCls)
     * OtherCls
     * ----------------------------------------------------------
     * Deleting SubCls should delete restriction as well
     */
    public void testJimWang() {
        OWLNamedClass superCls = owlModel.createOWLNamedSubclass("SuperCls", owlModel.getOWLThingClass());
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("OtherCls");
        OWLProperty property = owlModel.createOWLObjectProperty("property");

        int oldCount = owlModel.getClsCount();
        OWLNamedClass subCls = owlModel.createOWLNamedSubclass("SubCls", superCls);
        subCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, otherCls));

        subCls.delete();
        assertEquals(oldCount, owlModel.getClsCount());
    }


    /**
     * Person
     * PWAD [ * children !Girl  -> Delete PWAD should delete all depending anons
     */
    public void testDeleteClsWithAllRestriction() {
        RDFSClass personClass = owlModel.createOWLNamedClass("Person");
        OWLProperty childrenProperty = owlModel.createOWLObjectProperty("children");
        childrenProperty.addUnionDomainClass(personClass);
        RDFSClass girlClass = owlModel.createOWLNamedClass("Girl");
        RDFSClass personWithAntiDaughtersClass = owlModel.createOWLNamedClass("PersonWithAntiDaughters");
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(girlClass);
        OWLAllValuesFrom allRestriction = owlModel.createOWLAllValuesFrom(childrenProperty, complementCls);
        personWithAntiDaughtersClass.addSuperclass(allRestriction);
        int oldFrameCount = owlModel.getFrameCount();
        personWithAntiDaughtersClass.delete();
        assertEquals(oldFrameCount - 3, owlModel.getFrameCount());
    }


    public void testDeleteAnonymousEquivalentClass() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass girlCls = owlModel.createOWLNamedClass("Girl");
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(girlCls);
        personCls.addEquivalentClass(complementCls);
        int oldFrameCount = owlModel.getFrameCount();
        personCls.removeEquivalentClass(complementCls);
        assertEquals(oldFrameCount - 1, owlModel.getFrameCount());
    }


    public void testDeleteEnumerations() {
        OWLNamedClass colorCls = owlModel.createOWLNamedClass("Color");
        Instance red = owlModel.createInstance("Red", colorCls);
        Instance green = owlModel.createInstance("Green", colorCls);
        OWLNamedClass trafficLightColors = owlModel.createOWLNamedClass("TLC");
        List instances = Arrays.asList(new Instance[]{red, green});
        OWLEnumeratedClass enumerationCls = owlModel.createOWLEnumeratedClass(instances);
        trafficLightColors.addEquivalentClass(enumerationCls);
        int oldFrameCount = owlModel.getFrameCount();
        trafficLightColors.removeEquivalentClass(enumerationCls);
        assertEquals(oldFrameCount - 3, owlModel.getFrameCount());
    }


    public void testDeleteDefinedEnumeration() {
        int oldFrameCount = owlModel.getFrameCount();
        OWLNamedClass colorCls = owlModel.createOWLNamedClass("Color");
        Instance red = owlModel.createInstance("Red", colorCls);
        Instance green = owlModel.createInstance("Green", colorCls);
        List instances = Arrays.asList(new Instance[]{red, green});
        OWLEnumeratedClass enumerationCls = owlModel.createOWLEnumeratedClass(instances);
        colorCls.addEquivalentClass(enumerationCls);
        red.delete();
        green.delete();
        assertEquals(0, colorCls.getInstances(true).size());
        assertEquals(0, enumerationCls.getInstances(true).size());
        colorCls.delete();
        assertEquals(oldFrameCount, owlModel.getFrameCount());
    }


    public void testDeleteSubclassesAutomatically() {
        int classCount = owlModel.getClsCount();
        OWLNamedClass superclass = owlModel.createOWLNamedClass("Superclass");
        owlModel.createOWLNamedSubclass("Subclass", superclass);
        superclass.delete();
        assertEquals(classCount, owlModel.getClsCount());
    }


    /**
     * SuperCls  (= SubClsA | SubClsB)
     * SubClsA
     * SubClsB
     * OtherCls
     * Cls  (= OtherCls   &   ? slot (? slot SubCls))
     * ------------------------------------------------------------------------
     * After deleting SuperCls, the restrictions should be deleted both.
     */
    public void testDeleteClassWithNestedRestrictions() {
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("OtherCls");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Cls", otherCls);
        int oldFrameCount = owlModel.getFrameCount();
        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperCls");
        OWLNamedClass subClsA = owlModel.createOWLNamedSubclass("SubClsA", superCls);
        OWLNamedClass subClsB = owlModel.createOWLNamedSubclass("SubClsB", superCls);
        OWLUnionClass unionCls = owlModel.createOWLUnionClass();
        unionCls.addOperand(subClsB);
        unionCls.addOperand(subClsA);
        superCls.addEquivalentClass(unionCls);
        assertSize(1, cls.getSuperclasses(false));
        OWLSomeValuesFrom nestedRestriction = owlModel.createOWLSomeValuesFrom(slot, subClsA);
        OWLSomeValuesFrom restriction = owlModel.createOWLSomeValuesFrom(slot, nestedRestriction);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(otherCls);
        intersectionCls.addOperand(restriction);
        cls.addEquivalentClass(intersectionCls);
        assertSize(2, cls.getSuperclasses(false));
        superCls.delete();
        assertSize(1, cls.getSuperclasses(false));
        assertEquals(oldFrameCount, owlModel.getFrameCount());
    }


    public void testDeleteSubclasses() {
        int classCount = owlModel.getClsCount();
        OWLNamedClass cls = owlModel.createOWLNamedClass("Super");
        OWLNamedClass subCls = owlModel.createOWLNamedSubclass("Sub", cls);
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        subCls.addSuperclass(owlModel.createOWLMinCardinality(slot, 1));
        cls.delete();
        assertEquals(classCount, owlModel.getClsCount());
    }


    public void testDeleteClassUsedInDomains() {
        OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
        OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
        OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLUnionClass unionClass = owlModel.createOWLUnionClass();
        unionClass.addOperand(clsA);
        unionClass.addOperand(clsB);
        unionClass.addOperand(clsC);
        property.setDomain(unionClass);
        clsB.delete();
        RDFSClass newDomain = property.getDomain(false);
        assertTrue(newDomain instanceof OWLUnionClass);
        OWLUnionClass newUnionClass = (OWLUnionClass) newDomain;
        assertContains(clsA, newUnionClass.getOperands());
        assertContains(clsC, newUnionClass.getOperands());
        assertSize(2, newUnionClass.getOperands());
    }
}
