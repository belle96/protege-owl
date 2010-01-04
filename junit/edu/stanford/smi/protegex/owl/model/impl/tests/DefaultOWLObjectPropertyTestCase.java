package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLObjectPropertyTestCase extends AbstractJenaTestCase {

    public void testDefaultObjectProperty() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        assertFalse(property.isFunctional());
        assertFalse(property.isInverseFunctional());
        assertFalse(property.isSymmetric());
        assertFalse(property.isTransitive());
        assertFalse(property.isAnnotationProperty());
        assertTrue(((Slot) property).getAllowsMultipleValues());
    }


    public void testRange() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        assertFalse(property.hasDatatypeRange());
        assertTrue(property.hasObjectRange());
    }


    public void testUnionRange() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        property.addUnionRangeClass(a);
        assertSize(1, property.getUnionRangeClasses());
        assertEquals(a, property.getRange());
        property.addUnionRangeClass(b);
        assertSize(2, property.getUnionRangeClasses());
        assertContains(a, property.getUnionRangeClasses());
        assertContains(b, property.getUnionRangeClasses());
        property.removeUnionRangeClass(a);
        assertSize(1, property.getUnionRangeClasses());
        assertEquals(b, property.getRange());
    }


    public void testSubpropertyRange() {

        OWLObjectProperty superproperty = owlModel.createOWLObjectProperty("super");
        OWLObjectProperty subproperty = owlModel.createOWLObjectProperty("sub");
        subproperty.addSuperproperty(superproperty);

        assertNull(superproperty.getRange());
        Slot valueTypeSlot = owlModel.getSlot(Model.Slot.VALUE_TYPE);
        assertEquals(ValueType.INSTANCE, ((Slot) superproperty).getValueType());
        assertEquals(ValueType.INSTANCE, ((Slot) subproperty).getValueType());
        assertNull(((Slot) subproperty).getDirectOwnSlotValue(valueTypeSlot));

        RDFSClass range = owlModel.getOWLNamedClassClass();

        subproperty.setRange(range);
        assertEquals(range, subproperty.getRange());
        subproperty.setRange(null);
        assertEquals(null, ((Slot) subproperty).getDirectOwnSlotValue(valueTypeSlot));

        superproperty.setRange(range);
        subproperty.setRange(range);
        subproperty.setRange(null);
        assertEquals(null, ((Slot) subproperty).getDirectOwnSlotValue(valueTypeSlot));

        subproperty.removeSuperproperty(superproperty);
        assertEquals(ValueType.INSTANCE, ((Slot) subproperty).getValueType());
    }


    public void testSymmetricProperty() {
        RDFSClass metaclass = owlModel.getRDFSNamedClass(OWLNames.Cls.SYMMETRIC_PROPERTY);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        assertFalse(property.isSymmetric());
        property.setSymmetric(true);
        assertTrue(property.isSymmetric());
        assertSize(2, property.getProtegeTypes());
        assertTrue(property.hasProtegeType(metaclass));
        property.setSymmetric(false);
        assertFalse(property.isSymmetric());
        assertSize(1, property.getProtegeTypes());
        property.addProtegeType(metaclass);
        assertTrue(property.isSymmetric());
    }


    public void testTransitiveProperty() {
        RDFSClass metaclass = owlModel.getRDFSNamedClass(OWLNames.Cls.TRANSITIVE_PROPERTY);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        assertFalse(property.isTransitive());
        property.setTransitive(true);
        assertTrue(property.isTransitive());
        assertSize(2, property.getProtegeTypes());
        assertTrue(property.hasProtegeType(metaclass));
        property.setTransitive(false);
        assertFalse(property.isTransitive());
        assertSize(1, property.getProtegeTypes());
        property.addProtegeType(metaclass);
        assertTrue(property.isTransitive());
    }


    public void testInversePropertyWithSubproperties() {
        OWLObjectProperty a = owlModel.createOWLObjectProperty("a");
        OWLObjectProperty b = owlModel.createOWLObjectProperty("b");
        b.addSuperproperty(a);
        OWLObjectProperty c = owlModel.createOWLObjectProperty("c");
        assertSize(1, b.getSuperproperties(false));
        b.setInverseProperty(c);
        assertSize(1, b.getSuperproperties(false));
    }
}
