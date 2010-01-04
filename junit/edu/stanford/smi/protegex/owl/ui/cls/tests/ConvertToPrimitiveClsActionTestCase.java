package edu.stanford.smi.protegex.owl.ui.cls.tests;

import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.cls.ConvertToPrimitiveClassAction;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConvertToPrimitiveClsActionTestCase extends AbstractJenaTestCase {

    public void testConvertEquivalentClassToPrimitive() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("OtherCls");
        cls.setDefinition(otherCls);
        cls.removeSuperclass(owlThing);
        ConvertToPrimitiveClassAction.performAction(cls);
        assertNull(cls.getDefinition());
        assertTrue(cls.isSubclassOf(otherCls));
        assertSize(1, cls.getSuperclasses(false));
    }


    public void testConvertIntersectionDefinitionToPrimitive() {
        OWLNamedClass superCls = owlModel.createOWLNamedClass("Super");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Cls", superCls);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("slot");
        property.addUnionDomainClass(cls);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(superCls);
        intersectionCls.addOperand(owlModel.createOWLMinCardinality(property, 1));
        cls.addEquivalentClass(intersectionCls);
        assertSize(2, cls.getSuperclasses(false));
        ConvertToPrimitiveClassAction.performAction(cls);
        assertNull(cls.getDefinition());
        Collection superClses = cls.getSuperclasses(false);
        assertSize(2, superClses);
        superClses.remove(superCls);
        assertSize(1, superClses);
        assertTrue(superClses.iterator().next() instanceof OWLMinCardinality);
    }
}
