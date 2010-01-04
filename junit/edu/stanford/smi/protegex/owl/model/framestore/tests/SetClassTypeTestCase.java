package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SetClassTypeTestCase extends AbstractJenaTestCase {

    public void testChangeNamedClsToRDFSClassSuperclasses() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Parent");
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("hasChildren");
        cls.addSuperclass(owlModel.createOWLMinCardinality(slot, 1));
        assertSize(2, cls.getSuperclasses(false));
        cls.setProtegeType(owlModel.getRDFSNamedClassClass());
        assertSize(1, cls.getSuperclasses(false));
    }


    public void testChangeNamedClsToRDFSClassEquivalentClasses() {
        OWLNamedClass superCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Parent", superCls);
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("hasChildren");
        OWLMinCardinality restriction = owlModel.createOWLMinCardinality(slot, 1);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(superCls);
        intersectionCls.addOperand(restriction);
        cls.addEquivalentClass(intersectionCls);
        assertSize(2, cls.getSuperclasses(false));
        cls.setProtegeType(owlModel.getRDFSNamedClassClass());
        assertSize(1, cls.getSuperclasses(false));
        assertEquals(superCls, cls.getSuperclasses(false).iterator().next());
    }
}
