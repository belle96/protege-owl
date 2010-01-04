package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class GetExpressionRootTestCase extends AbstractJenaTestCase {

    /**
     * !A           -> getExpressionRoot should deliver this
     * Person
     */
    public void testThisIsRoot() {
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(aCls);
        personCls.addSuperclass(complementCls);
        assertEquals(complementCls, complementCls.getExpressionRoot());
    }


    /**
     * A & !B     ->  OWLComplementClass.getExpressionRoot should deliver OWLIntersectionClass
     * Person
     */
    public void testOWLIntersectionClass() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(bCls);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(aCls);
        intersectionCls.addOperand(complementCls);
        personCls.addSuperclass(intersectionCls);
        assertEquals(intersectionCls, intersectionCls.getExpressionRoot());
        assertEquals(intersectionCls, complementCls.getExpressionRoot());
    }


    /**
     * A | !(* children !A)   -> Asking for !A should return OWLUnionClass
     */
    public void testDeepOWLUnionClass() {
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("children");
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(aCls);
        OWLAllValuesFrom allRestriction = owlModel.createOWLAllValuesFrom(slot, complementCls);
        OWLComplementClass complementOfAll = owlModel.createOWLComplementClass(allRestriction);
        OWLUnionClass unionCls = owlModel.createOWLUnionClass();
        unionCls.addOperand(aCls);
        unionCls.addOperand(complementOfAll);
        assertEquals(unionCls, complementCls.getExpressionRoot());
        assertEquals(unionCls, allRestriction.getExpressionRoot());
        assertEquals(unionCls, complementOfAll.getExpressionRoot());
        assertEquals(unionCls, unionCls.getExpressionRoot());
    }
}
