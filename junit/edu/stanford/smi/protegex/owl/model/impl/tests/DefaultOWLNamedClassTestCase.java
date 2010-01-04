package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLNamedClassTestCase extends AbstractJenaTestCase {


    public void testGetHasValue() {
        OWLNamedClass superclass = owlModel.createOWLNamedClass("Superclass");
        OWLNamedClass subclass = owlModel.createOWLNamedSubclass("Subclass", superclass);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        assertNull(superclass.getHasValue(property));
        assertNull(subclass.getHasValue(property));
        superclass.addSuperclass(owlModel.createOWLHasValue(property, owlThing));
        assertEquals(owlThing, superclass.getHasValue(property));
        assertEquals(owlThing, subclass.getHasValue(property));
    }


    public void testGetRestrictionsAllValuesFrom() {
        OWLNamedClass superclass = owlModel.createOWLNamedClass("Superclass");
        OWLNamedClass subclass = owlModel.createOWLNamedSubclass("Subclass", superclass);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass otherClass = owlModel.createOWLNamedClass("Other");
        property.setRange(otherClass);
        assertEquals(otherClass, subclass.getAllValuesFrom(property));

        OWLAllValuesFrom superRestriction = owlModel.createOWLAllValuesFrom(property, subclass);
        superclass.addSuperclass(superRestriction);
        OWLAllValuesFrom subRestriction = owlModel.createOWLAllValuesFrom(property, subclass);
        subclass.addSuperclass(subRestriction);
        assertSize(1, superclass.getRestrictions(true));
        assertContains(superRestriction, superclass.getRestrictions(true));
        assertSize(1, subclass.getRestrictions(false));
        assertSize(1, subclass.getRestrictions(true));
        assertContains(subRestriction, subclass.getRestrictions(true));

        assertEquals(subclass, subclass.getAllValuesFrom(property));
    }


    public void testGetRestrictionsMinCardinality() {
        OWLNamedClass superclass = owlModel.createOWLNamedClass("Superclass");
        OWLNamedClass subclass = owlModel.createOWLNamedSubclass("Subclass", superclass);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        assertEquals(0, subclass.getMinCardinality(property));

        OWLMinCardinality superMinCardinality = owlModel.createOWLMinCardinality(property, 1);
        superclass.addSuperclass(superMinCardinality);
        OWLMaxCardinality subMaxCardinality = owlModel.createOWLMaxCardinality(property, 2);
        subclass.addSuperclass(subMaxCardinality);
        assertSize(1, superclass.getRestrictions(true));
        assertContains(superMinCardinality, superclass.getRestrictions(true));
        assertSize(2, subclass.getRestrictions(true));
        assertContains(superMinCardinality, subclass.getRestrictions(true));
        assertContains(subMaxCardinality, subclass.getRestrictions(true));

        OWLMinCardinality subMinCardinality = owlModel.createOWLMinCardinality(property, 2);
        subclass.addSuperclass(subMinCardinality);
        assertSize(2, subclass.getRestrictions(true));
        assertContains(subMinCardinality, subclass.getRestrictions(true));
        assertContains(subMaxCardinality, subclass.getRestrictions(true));
        assertEquals(2, subclass.getMinCardinality(property));
    }


    public void testGetRestrictionsMaxCardinality() {
        OWLNamedClass superclass = owlModel.createOWLNamedClass("Superclass");
        OWLNamedClass subclass = owlModel.createOWLNamedSubclass("Subclass", superclass);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        assertEquals(-1, subclass.getMaxCardinality(property));
        OWLMaxCardinality superRestriction = owlModel.createOWLMaxCardinality(property, 3);
        superclass.addSuperclass(superRestriction);

        assertSize(0, subclass.getRestrictions(false));
        assertSize(1, subclass.getRestrictions(true));
        assertContains(superRestriction, subclass.getRestrictions(true));

        OWLMaxCardinality subRestriction = owlModel.createOWLMaxCardinality(property, 2);
        subclass.addSuperclass(subRestriction);

        assertSize(1, subclass.getRestrictions(true));
        assertContains(subRestriction, subclass.getRestrictions(true));

        assertEquals(2, subclass.getMaxCardinality(property));

        property.setFunctional(true);
        assertEquals(1, subclass.getMaxCardinality(property));
    }


    public void testGetSubclassesOfOWLThing() {
        Collection subs = owlThing.getSubclasses(false);
        for (Iterator it = subs.iterator(); it.hasNext();) {
            Object subclass = it.next();
            assertTrue(subclass instanceof RDFSNamedClass);
        }
        assertEquals(subs.size(), owlThing.getSubclassCount());
    }
}
