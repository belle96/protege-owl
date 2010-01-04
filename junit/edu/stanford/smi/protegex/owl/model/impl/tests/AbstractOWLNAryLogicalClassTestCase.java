package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AbstractOWLNAryLogicalClassTestCase extends AbstractJenaTestCase {

    public void testHasSameOperands() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        RDFProperty property = owlModel.createRDFProperty("property");
        OWLIntersectionClass a = owlModel.createOWLIntersectionClass();
        a.addOperand(cls);
        a.addOperand(owlModel.createOWLComplementClass(cls));
        a.addOperand(owlModel.createOWLMinCardinality(property, 1));
        OWLIntersectionClass b = owlModel.createOWLIntersectionClass();
        b.addOperand(owlModel.createOWLComplementClass(cls));
        b.addOperand(owlModel.createOWLMinCardinality(property, 1));
        b.addOperand(cls);
        assertTrue(a.hasSameOperands(b));
        assertTrue(b.hasSameOperands(a));
        b.addOperand(owlModel.createOWLMaxCardinality(property, 2));
        assertFalse(a.hasSameOperands(b));
        assertFalse(b.hasSameOperands(a));
    }
}
