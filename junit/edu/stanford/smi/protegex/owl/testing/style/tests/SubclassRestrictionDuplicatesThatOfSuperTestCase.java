package edu.stanford.smi.protegex.owl.testing.style.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.testing.style.SubclassesRestrictionDuplicatesThatOfSuperTest;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         07-Feb-2006
 */
public class SubclassRestrictionDuplicatesThatOfSuperTestCase extends AbstractJenaTestCase {

    public void testMinCardinalityDuplicated(){
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        OWLNamedClass c = owlModel.createOWLNamedClass("C");
        OWLNamedClass d = owlModel.createOWLNamedClass("D");

        b.addSuperclass(a);
        c.addSuperclass(b);

        OWLObjectProperty p = owlModel.createOWLObjectProperty("p");

        OWLMinCardinality restrA = owlModel.createOWLMinCardinality(p, 1, d);
        OWLMinCardinality restrB = owlModel.createOWLMinCardinality(p, 1, d);
        OWLAllValuesFrom restrC = owlModel.createOWLAllValuesFrom(p, d);

        assertTrue(restrA.equalsStructurally(restrB));

        a.addSuperclass(restrA);
        a.addSuperclass(restrC);
        c.addSuperclass(restrB);

        assertTrue(SubclassesRestrictionDuplicatesThatOfSuperTest.fails(c));

        SubclassesRestrictionDuplicatesThatOfSuperTest.fix(c);

        assertFalse(SubclassesRestrictionDuplicatesThatOfSuperTest.fails(c));
    }

    public void testExistentialDuplicated(){
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        b.addSuperclass(a);

        OWLNamedClass c = owlModel.createOWLNamedClass("C");
        OWLObjectProperty p = owlModel.createOWLObjectProperty("p");

        OWLSomeValuesFrom restrA = owlModel.createOWLSomeValuesFrom(p, c);
        OWLSomeValuesFrom restrB = owlModel.createOWLSomeValuesFrom(p, c);

        a.addSuperclass(restrA);
        b.addSuperclass(restrB);

        assertTrue(SubclassesRestrictionDuplicatesThatOfSuperTest.fails(b));

        SubclassesRestrictionDuplicatesThatOfSuperTest.fix(b);

        assertFalse(SubclassesRestrictionDuplicatesThatOfSuperTest.fails(b));
    }

}
