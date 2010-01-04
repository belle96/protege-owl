package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLUnionClassTestCase extends AbstractJenaTestCase {

    public void testSimpleUnion() {

        RDFProperty unionOfProperty = owlModel.getRDFProperty(OWLNames.Slot.UNION_OF);

        OWLNamedClass clsA = owlModel.createOWLNamedClass("ClassA");
        OWLNamedClass clsB = owlModel.createOWLNamedClass("ClassB");

        OWLUnionClass c = owlModel.createOWLUnionClass();
        assertEquals(unionOfProperty, c.getOperandsProperty());
        c.addOperand(clsA);
        c.addOperand(clsB);
        assertSize(2, c.getOperands());
        assertSize(1, c.getPropertyValues(unionOfProperty));
        RDFList list = (RDFList) c.getPropertyValue(unionOfProperty);
        assertContains(clsA, list.getValues());
        assertContains(clsB, list.getValues());
        assertTrue(list.isClosed());

        c.removeOperand(clsB);
        assertSize(1, c.getOperands());
        assertContains(clsA, c.getOperands());
        list = (RDFList) c.getPropertyValue(unionOfProperty);
        assertTrue(list.isClosed());
    }
}
