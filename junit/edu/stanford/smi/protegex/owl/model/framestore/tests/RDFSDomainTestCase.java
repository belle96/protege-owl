package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFSDomainTestCase extends AbstractJenaTestCase {

    public void testSimpleDomain() {
        RDFProperty property = owlModel.createRDFProperty("property");
        RDFSClass cls = owlModel.createRDFSNamedClass("Class");
        property.setDomain(cls);
        assertSize(1, property.getDomains(false));
        assertContains(cls, property.getDomains(false));
        assertSize(1, ((Slot) property).getDirectDomain());
        assertContains(cls, ((Slot) property).getDirectDomain());
    }


    public void testChangeTemplateSlots() {
        RDFProperty property = owlModel.createRDFProperty("property");
        RDFSClass clsA = owlModel.createRDFSNamedClass("ClassA");
        RDFSClass clsB = owlModel.createRDFSNamedClass("ClassB");
        ((Cls) owlThing).removeDirectTemplateSlot(property);
        ((Cls) clsA).addDirectTemplateSlot(property);
        assertEquals(clsA, property.getDomain(false));
        ((Cls) clsB).addDirectTemplateSlot(property);
        assertTrue(property.getDomain(false) instanceof OWLUnionClass);
    }

    @SuppressWarnings("deprecation")
	public void testDomainCorrectOnDeletingClass(){
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass owlThing = owlModel.getOWLThingClass();
        RDFProperty p = owlModel.createOWLObjectProperty("p");

        assertSize(1, p.getDirectDomain());
        assertContains(owlThing, p.getDirectDomain());

        p.setDomain(a);

        assertSame(p.getDomain(false), a);

        a.delete();

        assertSize(1, p.getDirectDomain());
        assertContains(owlThing, p.getDirectDomain());
    }
}
