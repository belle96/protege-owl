package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collections;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeleteListEntryTestCase extends AbstractJenaTestCase {

    /**
     * All ListInstances that have a deleted instance as their rdf:first
     * must be deleted too.
     */
    public void testDeleteEntries() {
        int oldListCount = owlModel.getRDFListClass().getInstanceCount(false);
        RDFList li = owlModel.createRDFList();
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        property.setUnionRangeClasses(Collections.singleton(owlModel.getRDFListClass()));
        property.setDomain(cls);
        RDFResource instance = cls.createInstance("instance");
        instance.setPropertyValue(property, li);
        Instance a = cls.createInstance("a");
        Instance b = cls.createInstance("b");
        Instance c = cls.createInstance("c");
        li.append(a);
        li.append(b);
        li.append(c);
        assertEquals("rdf:List (a, b, c)", li.getBrowserText());
        b.delete();
        assertEquals("rdf:List (a, c)", li.getBrowserText());
        c.delete();
        assertEquals("rdf:List (a)", li.getBrowserText());
        assertEquals(li, instance.getPropertyValue(property));
        a.delete();
        assertNull(instance.getPropertyValue(property));
        assertEquals(oldListCount, owlModel.getRDFListClass().getInstanceCount(false));
    }


    public void testDeleteFirstEntry() {
        RDFList li = owlModel.createRDFList();
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        RDFProperty slot = owlModel.createOWLObjectProperty("slot");
        slot.setUnionRangeClasses(Collections.singleton(owlModel.getRDFListClass()));
        slot.addUnionDomainClass(cls);
        RDFResource instance = cls.createInstance("instance");
        instance.setPropertyValue(slot, li);
        Instance a = cls.createInstance("a");
        Instance b = cls.createInstance("b");
        li.append(a);
        li.append(b);
        RDFList rest = li.getRest();
        assertEquals("rdf:List (a, b)", li.getBrowserText());
        assertEquals(li, instance.getPropertyValue(slot));
        a.delete();
        assertEquals("rdf:List (b)", rest.getBrowserText());
        assertEquals(rest, instance.getPropertyValue(slot));
    }


    public void testDeleteInstanceWithListAsValue() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        property.setRange(owlModel.getRDFListClass());
        property.setDomain(cls);
        RDFResource a = cls.createInstance("a");
        RDFResource b = cls.createInstance("b");
        int frameCount = owlModel.getFrameCount();
        RDFResource instance = cls.createInstance("instance");
        RDFList li = owlModel.createRDFList();
        instance.setPropertyValue(property, li);
        li.append(a);
        li.append(b);
        instance.delete();
        assertEquals(frameCount, owlModel.getFrameCount());
    }
}
