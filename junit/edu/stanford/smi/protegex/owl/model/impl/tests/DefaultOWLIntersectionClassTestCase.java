package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLIntersectionClassTestCase extends AbstractJenaTestCase {

    public void testSimpleIntersection() {

        OWLNamedClass clsA = owlModel.createOWLNamedClass("ClassA");
        OWLNamedClass clsB = owlModel.createOWLNamedClass("ClassB");

        OWLIntersectionClass c = owlModel.createOWLIntersectionClass();
        RDFProperty intersectionOfProperty = owlModel.getRDFProperty(OWLNames.Slot.INTERSECTION_OF);
        assertEquals(intersectionOfProperty, c.getOperandsProperty());
        c.addOperand(clsA);
        c.addOperand(clsB);
        assertSize(2, c.getOperands());
        assertSize(1, c.getPropertyValues(intersectionOfProperty));
        assertContains(clsA, c.getOperands());
        assertContains(clsB, c.getOperands());

        c.removeOperand(clsB);
        assertSize(1, c.getOperands());
        assertContains(clsA, c.getOperands());
    }
}
