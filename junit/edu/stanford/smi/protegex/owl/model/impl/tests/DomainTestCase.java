package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * Tests for properties that don't have any domain defined.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DomainTestCase extends AbstractJenaTestCase {

    public void testSubSlotDomainDefined() {

        OWLObjectProperty a = owlModel.createOWLObjectProperty("a");
        a.setDomainDefined(false);
        assertFalse(a.isDomainDefined());
        assertFalse(a.isDomainDefined(true));
        assertSize(1, a.getUnionDomain());

        OWLObjectProperty b = (OWLObjectProperty) owlModel.createSubproperty("b", a);
        OWLObjectProperty c = (OWLObjectProperty) owlModel.createSubproperty("c", b);
        assertFalse(b.isDomainDefined());
        assertFalse(b.isDomainDefined(true));
        assertFalse(c.isDomainDefined());
        assertFalse(c.isDomainDefined(true));

        b.setDomainDefined(true);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        b.addUnionDomainClass(cls);
        assertTrue(b.isDomainDefined());
        assertTrue(b.isDomainDefined(true));
        assertFalse(c.isDomainDefined());
        assertTrue(c.isDomainDefined(true));

        assertContains(owlThing, a.getUnionDomain());
        assertContains(cls, b.getUnionDomain());
    }


    public void testSetDomainDefined() {
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("Test");
        assertFalse(slot.isDomainDefined());
        //assertTrue(slot.getUnionDomain().isEmpty());
        slot.setDomainDefined(false);
        assertFalse(slot.isDomainDefined());
        assertEquals(owlModel.getRootClses(), slot.getUnionDomain());
        slot.setDomainDefined(true);
        assertTrue(slot.isDomainDefined());
        assertTrue(slot.getUnionDomain().size() == 1);
        assertTrue(slot.getUnionDomain().contains(owlModel.getOWLThingClass()));
    }


    public void testForgetOldDomain() {
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("Test");
        slot.setDomainDefined(true);
        RDFSNamedClass cls = owlModel.createOWLNamedClass("Cls");
        slot.addUnionDomainClass(cls);
        assertSize(1, slot.getUnionDomain());
        assertEquals(cls, slot.getUnionDomain().iterator().next());
        slot.setDomainDefined(false);
        assertEquals(owlModel.getRootClses(), slot.getUnionDomain());
    }


    public void testSlotWithoutDomainIsTemplateSlotEverywhere() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        int baseCount = cls.getUnionDomainProperties(true).size();
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("Test");
        slot.setDomainDefined(false);
        assertSize(0, cls.getUnionDomainProperties());
        assertSize(baseCount + 1, cls.getUnionDomainProperties(true));
        assertContains(slot, cls.getUnionDomainProperties(true).iterator());
        slot.setDomainDefined(true);
        assertSize(baseCount + 1, cls.getUnionDomainProperties(true));
        assertContains(slot, cls.getUnionDomainProperties(true).iterator());
    }


    public void testDomainDefinedOfSubproperty() {

        OWLObjectProperty superproperty = owlModel.createOWLObjectProperty("super");
        assertFalse(superproperty.isDomainDefined());

        OWLObjectProperty subproperty = owlModel.createOWLObjectProperty("sub");
        assertFalse(subproperty.isDomainDefined());

        subproperty.addSuperproperty(superproperty);
        assertFalse(subproperty.isDomainDefined());
        subproperty.setDomainDefined(true);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        subproperty.addUnionDomainClass(cls);
        assertFalse(superproperty.isDomainDefined());
        assertTrue(subproperty.isDomainDefined());
        assertTrue(subproperty.isDomainDefined(true));
    }


    public void testDomainDefinedOfSubproperty2() {
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        RDFProperty superproperty = owlModel.createRDFProperty("super");
        RDFProperty subproperty = owlModel.createRDFProperty("sub");
        subproperty.addSuperproperty(superproperty);
        assertFalse(superproperty.isDomainDefined());
        superproperty.setDomain(cls);
        assertTrue(superproperty.isDomainDefined());
        assertFalse(subproperty.isDomainDefined());
        assertTrue(subproperty.isDomainDefined(true));
    }
}
