package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class GetReferringAnonymousClassesTestCase extends AbstractJenaTestCase {


    public void testOWLComplementClass() {
        OWLNamedClass namedCls = owlModel.createOWLNamedClass("Person");
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(namedCls);
        assertSize(1, namedCls.getReferringAnonymousClasses());
        assertContains(complementCls, namedCls.getReferringAnonymousClasses());
    }


    public void testOWLEnumeratedClass() {
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        RDFResource a = (RDFResource) aCls.createInstance("a");
        RDFResource b = aCls.createRDFIndividual("b");
        OWLEnumeratedClass enumeratedClass = owlModel.createOWLEnumeratedClass();
        enumeratedClass.addOneOf(a);
        enumeratedClass.addOneOf(b);
        assertSize(1, a.getReferringAnonymousClasses());
        assertContains(enumeratedClass, a.getReferringAnonymousClasses());
    }


    public void testOWLMinCardinality() {
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        OWLMinCardinality restriction = owlModel.createOWLMinCardinality(slot, 1);
        assertSize(1, slot.getReferringAnonymousClasses());
        assertContains(restriction, slot.getReferringAnonymousClasses());
    }


    public void testOWLUnionClass() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Person");
        OWLNamedClass otherClass = owlModel.createOWLNamedClass("Other");
        OWLUnionClass unionClass = owlModel.createOWLUnionClass();
        unionClass.addOperand(otherClass);
        unionClass.addOperand(namedClass);
        assertSize(1, namedClass.getReferringAnonymousClasses());
        assertContains(unionClass, namedClass.getReferringAnonymousClasses());
    }
}
