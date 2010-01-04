package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * A TestCase to test whether depending anonymous classes are deleted when
 * a slot is deleted.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeletePropertyTestCase extends AbstractJenaTestCase {

    /**
     * owl:Thing
     * Person
     * children <= 7   -> delete children Slot
     * ------------------------------------------------------
     * Should delete the restriction
     */
    public void testDeleteRestriction() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty children = owlModel.createOWLObjectProperty("children");
        int clsCount = owlModel.getClsCount();
        OWLRestriction restriction = owlModel.createOWLMaxCardinality(children, 7);
        cls.addSuperclass(restriction);
        children.delete();
        assertEquals(clsCount, owlModel.getClsCount());
    }


    /**
     * owl:Thing
     * Person
     * !(children <= 7)   -> delete children Slot
     * ------------------------------------------------------
     * Should delete the whole expression
     */
    public void testDeleteComplementOfRestriction() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty children = owlModel.createOWLObjectProperty("children");
        int clsCount = owlModel.getClsCount();
        OWLRestriction restriction = owlModel.createOWLMaxCardinality(children, 7);
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(restriction);
        cls.addSuperclass(complementCls);
        children.delete();
        assertEquals(clsCount, owlModel.getClsCount());
    }
}
