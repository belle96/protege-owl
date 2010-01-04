package edu.stanford.smi.protegex.owl.ui.cls.tests;

import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.cls.ConvertToDefinedClassAction;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConvertToDefinedClsActionTestCase extends AbstractJenaTestCase {

    public void testFailToConvertPrimitiveToDefinedIfOnlyThingIsSuperclass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        ConvertToDefinedClassAction.performAction(cls);
        assertNull(cls.getDefinition());
    }


    public void testSimpleConvertPrimitiveToDefined() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("slot");
        property.setDomain(cls);
        cls.addSuperclass(owlModel.createOWLMinCardinality(property, 1));
        ConvertToDefinedClassAction.performAction(cls);
        assertSize(1, cls.getEquivalentClasses());
        assertTrue(cls.getDefinition() instanceof OWLMinCardinality);
    }


    public void testConvertPrimitiveToDefinedWith2Operands() {
        OWLNamedClass superCls = owlModel.createOWLNamedClass("Super");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Cls", superCls);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("slot");
        property.setDomain(cls);
        cls.addSuperclass(owlModel.createOWLMinCardinality(property, 1));
        ConvertToDefinedClassAction.performAction(cls);
        assertSize(1, cls.getEquivalentClasses());
        assertTrue(cls.getDefinition() instanceof OWLIntersectionClass);
        OWLIntersectionClass intersectionCls = (OWLIntersectionClass) cls.getDefinition();
        Collection operands = new ArrayList(intersectionCls.getOperands());
        assertSize(2, operands);
        assertContains(superCls, operands);
        operands.remove(superCls);
        assertTrue(operands.iterator().next() instanceof OWLMinCardinality);
        assertTrue(cls.isSubclassOf(superCls));
        assertSize(2, cls.getSuperclasses(false));
    }
}
