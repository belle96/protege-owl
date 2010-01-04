package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFNames;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFListTestCase extends AbstractJenaTestCase {


    public void testAppend() {
        RDFList li = owlModel.createRDFList();
        assertEquals(0, li.size());
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        Instance a = cls.createInstance("a");
        Instance b = cls.createInstance("b");
        Instance c = cls.createInstance("c");
        li.append(a);
        assertEquals(1, li.size());
        assertEquals(a, li.getFirst());
        assertEquals(owlModel.getRDFNil(), li.getRest());
        li.append(b);
        assertEquals(2, li.size());
        assertEquals(a, li.getFirst());
        assertEquals(b, li.getRest().getFirst());
        assertEquals(owlModel.getRDFNil(), li.getRest().getRest());
        li.append(c);
        assertEquals(3, li.size());
        assertEquals(a, li.getFirst());
        assertEquals(b, li.getRest().getFirst());
        assertEquals(c, li.getRest().getRest().getFirst());
        assertEquals(owlModel.getRDFNil(), li.getRest().getRest().getRest());
        li.delete();
    }


    public void testCreateAndDeleteListInstanceManually() {
        Cls listCls = owlModel.getRDFListClass();
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        Instance first = cls.createInstance("first");
        Instance second = cls.createInstance("second");
        int count = owlModel.getFrameCount();
        RDFList head = (RDFList) listCls.createDirectInstance(null);
        assertNull(head.getFirst());
        assertNull(head.getRest());
        head.setFirst(first);
        assertEquals(first, head.getFirst());
        RDFList rest = (RDFList) listCls.createDirectInstance(null);
        head.setRest(rest);
        assertEquals(rest, head.getRest());
        rest.setFirst(second);
        Iterator it = head.getValues().iterator();
        assertEquals(first, it.next());
        assertEquals(second, it.next());
        assertFalse(it.hasNext());
        assertEquals(2, head.size());
        assertTrue(head.contains(second));
        head.delete();
        assertEquals(count, owlModel.getFrameCount());
    }


    public void testCreateListInstanceThroughOKB() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        Instance first = cls.createInstance("first");
        Instance second = cls.createInstance("second");
        Collection values = new ArrayList();
        values.add(first);
        values.add(second);
        RDFList head = owlModel.createRDFList(values.iterator());
        assertEquals(first, head.getFirst());
        Iterator it = head.getValues().iterator();
        assertEquals(first, it.next());
        assertEquals(second, it.next());
        assertFalse(it.hasNext());
        assertEquals(2, head.size());
        assertTrue(head.contains(second));
        assertEquals("rdf:List (first, second)", head.getBrowserText());
        head.delete();
    }


    public void testListSetup() {
        Cls listCls = owlModel.getRDFListClass();
        assertEquals(RDFNames.Cls.LIST, listCls.getName());
        assertEquals(1, listCls.getDirectInstanceCount());
        Frame nil = (Frame) listCls.getDirectInstances().iterator().next();
        assertEquals(owlModel.getInstance(RDFNames.Instance.NIL), nil);
        Slot firstSlot = owlModel.getSlot(RDFNames.Slot.FIRST);
        assertEquals(ValueType.ANY, firstSlot.getValueType());
        Slot restSlot = owlModel.getSlot(RDFNames.Slot.REST);
        assertEquals(ValueType.INSTANCE, restSlot.getValueType());
        assertEquals(1, restSlot.getAllowedClses().size());
        assertEquals(listCls, restSlot.getAllowedClses().iterator().next());
        assertFalse(listCls.isEditable());
        assertFalse(firstSlot.isEditable());
        assertFalse(restSlot.isEditable());
        assertFalse(nil.isEditable());
    }


    public void testSubclassOfList() {
        RDFSNamedClass listCls = owlModel.getRDFListClass();
        Cls subCls = owlModel.createRDFSNamedSubclass("MyList", listCls);
        Instance listInstance = subCls.createDirectInstance(null);
        assertTrue(listInstance instanceof RDFList);
    }


    public void testGetStart() {
        RDFList nodeA = owlModel.createRDFList();
        RDFList nodeB = owlModel.createRDFList();
        RDFList nodeC = owlModel.createRDFList();
        nodeA.setRest(nodeB);
        nodeB.setRest(nodeC);
        assertEquals(nodeA, nodeA.getStart());
        assertEquals(nodeA, nodeB.getStart());
        assertEquals(nodeA, nodeC.getStart());
    }
}
