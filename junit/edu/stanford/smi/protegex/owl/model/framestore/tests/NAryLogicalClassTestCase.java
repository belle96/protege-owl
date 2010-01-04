package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class NAryLogicalClassTestCase extends AbstractJenaTestCase {

    public void testAddOperands() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("A");
        OWLComplementClass complementClass = owlModel.createOWLComplementClass(namedClass);
        OWLUnionClass unionClass = owlModel.createOWLUnionClass();
        assertSize(0, unionClass.getOperands());
        unionClass.addOperand(namedClass);
        assertSize(1, unionClass.getOperands());
        assertContains(namedClass, unionClass.getOperands());
        assertTrue(unionClass.getPropertyValue(unionClass.getOperandsProperty()) instanceof RDFList);
        unionClass.addOperand(complementClass);
        assertSize(2, unionClass.getOperands());
        assertContains(namedClass, unionClass.getOperands());
        assertContains(complementClass, unionClass.getOperands());
        Object listValue = unionClass.getPropertyValue(unionClass.getOperandsProperty());
        assertTrue(listValue instanceof RDFList);
        RDFList list = (RDFList) listValue;
        List values = list.getValues();
        assertEquals(namedClass, values.get(0));
        assertEquals(complementClass, values.get(1));
    }


    public void testDeleteRDFLists() {
        int frameCount = owlModel.getFrameCount();
        OWLUnionClass unionClass = owlModel.createOWLUnionClass();
        unionClass.addOperand(owlThing);
        unionClass.addOperand(owlModel.createOWLComplementClass(owlThing));
        unionClass.delete();
        assertEquals(frameCount, owlModel.getFrameCount());
    }

    /*public void testRemoveOperandWhenClassDeleted() {
       OWLNamedClass namedClass = owlModel.createOWLNamedClass("A");
       OWLComplementClass complementClass = owlModel.createOWLComplementClass(namedClass);
       OWLUnionClass unionClass = owlModel.createOWLUnionClass(Arrays.asList(new RDFSClass[] {
           namedClass, complementClass
       }));
       assertSize(2, unionClass.getOperands());
       assertContains(namedClass, unionClass.getOperands());
       assertContains(complementClass, unionClass.getOperands());
       int oldListCount = owlModel.getRDFListClass().getInstanceCount(false);
       namedClass.delete();
       assertSize(1, unionClass.getOperands());
       assertContains(complementClass, unionClass.getOperands());
       int newListCount = owlModel.getRDFListClass().getInstanceCount(false);
       assertEquals(oldListCount, newListCount + 1);
   } */
}
