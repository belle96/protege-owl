package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultRDFPropertyTestCase extends AbstractJenaTestCase {

    public void testDefaultRDFProperty() {
        RDFProperty property = owlModel.createRDFProperty("property");
        assertFalse(property.isFunctional());
        assertFalse(property.isAnnotationProperty());
        assertNull(property.getRange());
        assertTrue(((Slot) property).getAllowsMultipleValues());
        assertEquals(ValueType.ANY, ((Slot) property).getValueType());
    }


    public void testFunctional() {
        RDFSNamedClass metaclass = owlModel.getRDFSNamedClass(OWLNames.Cls.FUNCTIONAL_PROPERTY);
        RDFProperty property = owlModel.createRDFProperty("property");
        assertFalse(property.isFunctional());
        assertSize(1, property.getProtegeTypes());
        assertTrue(((Slot) property).getAllowsMultipleValues());

        property.setFunctional(true);
        assertFalse(((Slot) property).getAllowsMultipleValues());
        assertTrue(property.isFunctional());
        assertSize(2, property.getProtegeTypes());
        assertContains(metaclass, property.getProtegeTypes());
        assertFalse(((Slot) property).getAllowsMultipleValues());

        property.setFunctional(false);
        assertTrue(((Slot) property).getAllowsMultipleValues());

        property.addProtegeType(metaclass);
        assertTrue(property.isFunctional());
        assertFalse(((Slot) property).getAllowsMultipleValues());
    }


    public void testFunctionalSuperproperty() {
        RDFSNamedClass metaclass = owlModel.getRDFSNamedClass(OWLNames.Cls.FUNCTIONAL_PROPERTY);
        RDFProperty superproperty = owlModel.createRDFProperty("super");
        RDFProperty subproperty = owlModel.createRDFProperty("sub");
        subproperty.addSuperproperty(superproperty);
        superproperty.setFunctional(true);
        assertFalse(subproperty.hasProtegeType(metaclass));
        assertTrue(subproperty.isFunctional());
    }


    public void testRangeRDFDatatype() {

        RDFProperty property = owlModel.createRDFProperty("property");
        assertNull(property.getRange());
        assertEquals(ValueType.ANY, ((Slot) property).getValueType());
        assertFalse(property.hasRange(false));
        assertFalse(property.hasDatatypeRange());
        assertFalse(property.hasObjectRange());

        property.setRange(owlModel.getXSDint());
        assertEquals(owlModel.getXSDint(), property.getRange(false));
        assertSize(1, property.getRanges(false));
        assertContains(owlModel.getXSDint(), property.getRanges(false));
        assertEquals(ValueType.INTEGER, ((Slot) property).getValueType());
        assertTrue(property.hasRange(false));
        assertTrue(property.hasDatatypeRange());
        assertFalse(property.hasObjectRange());

        property.setRange(owlModel.getXSDboolean());
        assertEquals(owlModel.getXSDboolean(), property.getRange(false));
        assertEquals(ValueType.BOOLEAN, ((Slot) property).getValueType());
        assertTrue(property.hasRange(false));
        assertTrue(property.hasDatatypeRange());
        assertFalse(property.hasObjectRange());

        property.setRange(null);
        assertSize(0, property.getRanges(false));
        assertEquals(ValueType.ANY, ((Slot) property).getValueType());
        assertFalse(property.hasRange(false));
        assertFalse(property.hasDatatypeRange());
        assertFalse(property.hasObjectRange());
    }


    public void testRangeRDFSClass() {

        RDFProperty property = owlModel.createRDFProperty("property");
        assertNull(property.getRange());
        assertEquals(ValueType.ANY, ((Slot) property).getValueType());

        RDFSNamedClass c = owlModel.createRDFSNamedClass("Class");
        property.setRange(c);
        assertSize(1, property.getRanges(false));
        assertContains(c, property.getRanges(false));
        assertEquals(c, property.getRange());
        assertEquals(ValueType.INSTANCE, ((Slot) property).getValueType());
    }


    public void testSubpropertyRange() {

        RDFProperty superproperty = owlModel.createRDFProperty("super");
        RDFProperty subproperty = owlModel.createRDFProperty("sub");
        subproperty.addSuperproperty(superproperty);

        assertNull(superproperty.getRange());
        Slot valueTypeSlot = owlModel.getSlot(Model.Slot.VALUE_TYPE);
        assertNull(((Slot) superproperty).getDirectOwnSlotValue(valueTypeSlot));
        assertNull(((Slot) subproperty).getDirectOwnSlotValue(valueTypeSlot));

        subproperty.setRange(owlModel.getXSDstring());
        assertEquals(ValueType.STRING, ((Slot) subproperty).getValueType());
        subproperty.setRange(null);
        assertNull(((Slot) subproperty).getDirectOwnSlotValue(valueTypeSlot));

        superproperty.setRange(owlModel.getXSDstring());
        assertEquals(ValueType.STRING, ((Slot) superproperty).getValueType());
        subproperty.setRange(owlModel.getXSDstring());
        assertEquals(ValueType.STRING, ((Slot) subproperty).getValueType());
        subproperty.setRange(null);
        assertNull(((Slot) subproperty).getDirectOwnSlotValue(valueTypeSlot));
    }


    public void testUnionRange() {

        RDFProperty property = owlModel.createRDFProperty("property");
        assertNull(property.getRange());
        assertEquals(ValueType.ANY, ((Slot) property).getValueType());

        RDFSNamedClass classA = owlModel.createRDFSNamedClass("ClassA");
        RDFSNamedClass classB = owlModel.createRDFSNamedClass("ClassB");
        Collection classes = new ArrayList();
        classes.add(classA);
        classes.add(classB);
        property.setUnionRangeClasses(classes);

        OWLUnionClass unionRange = (OWLUnionClass) property.getRange();
        assertSize(2, unionRange.getOperands());
        assertContains(classA, unionRange.getOperands());
        assertContains(classB, unionRange.getOperands());

        assertEquals(ValueType.INSTANCE, ((Slot) property).getValueType());
        assertSize(2, ((Slot) property).getAllowedClses());
        assertContains(classA, ((Slot) property).getAllowedClses());
        assertContains(classB, ((Slot) property).getAllowedClses());

        int oldClassCount = owlModel.getClsCount();

        property.setUnionRangeClasses(classes);

        assertEquals(ValueType.INSTANCE, ((Slot) property).getValueType());
        assertSize(2, ((Slot) property).getAllowedClses());
        assertContains(classA, ((Slot) property).getAllowedClses());
        assertContains(classB, ((Slot) property).getAllowedClses());

        assertFalse(unionRange.equals(property.getRange()));

        assertEquals(oldClassCount, owlModel.getClsCount());
    }


    public void testRDFSDomainAssignments() {
        RDFProperty property = owlModel.createRDFProperty("property");
        RDFSClass cls = owlModel.createRDFSNamedClass("Class");
        RDFSClass otherCls = owlModel.createRDFSNamedClass("OtherClass");
        property.setDomain(cls);
        assertEquals(cls, property.getDomain(false));
        assertSize(1, property.getDomains(false));
        assertContains(cls, property.getDomains(false));
        property.setDomain(null);
        assertSize(0, property.getDomains(false));
        Collection domains = Arrays.asList(new RDFSClass[]{
                cls, otherCls
        });
        property.setDomains(domains);
        assertSize(2, property.getDomains(false));
        assertContains(cls, property.getDomains(false));
        assertContains(otherCls, property.getDomains(false));
    }


    public void testRDFSDomainOfSubproperties() {
        RDFProperty superproperty = owlModel.createRDFProperty("superproperty");
        RDFProperty subproperty = owlModel.createRDFProperty("subproperty");
        subproperty.addSuperproperty(superproperty);
        subproperty.setDomainDefined(false);
        RDFSClass cls = owlModel.createRDFSNamedClass("Class");
        RDFSClass otherCls = owlModel.createRDFSNamedClass("OtherClass");
        superproperty.setDomain(cls);
        assertEquals(cls, superproperty.getDomain(false));
        assertEquals(cls, superproperty.getDomain(true));
        assertEquals(null, subproperty.getDomain(false));
        assertEquals(cls, subproperty.getDomain(true));
        subproperty.setDomain(otherCls);
        assertEquals(otherCls, subproperty.getDomain(false));
        assertEquals(otherCls, subproperty.getDomain(true));
        assertSize(1, subproperty.getDomains(true));
    }


    public void testUpdateDomainAfterRemovingSuperproperty() {
        RDFSNamedClass domainClass = owlModel.createRDFSNamedClass("Class");
        RDFProperty superProperty = owlModel.createRDFProperty("Super");
        superProperty.setDomain(domainClass);
        RDFProperty subProperty = owlModel.createRDFProperty("Sub");
        subProperty.addSuperproperty(superProperty);
        subProperty.setDomainDefined(false);
        assertFalse(subProperty.isDomainDefined());
        assertEquals(domainClass, subProperty.getDomain(true));
        Slot directDomainSlot = owlModel.getSlot(Model.Slot.DIRECT_DOMAIN);
        assertEquals(owlModel.getOWLThingClass(), ((Slot) subProperty).getDirectOwnSlotValue(directDomainSlot));
        subProperty.removeSuperproperty(superProperty);
        assertFalse(subProperty.isDomainDefined());
        assertSize(1, ((Slot) subProperty).getDirectOwnSlotValues(directDomainSlot));
        assertEquals(owlThing, ((Slot) subProperty).getDirectOwnSlotValue(directDomainSlot));
    }


    public void testRDFSDomainUnionClass() {
        RDFProperty property = owlModel.createRDFProperty("property");
        RDFSClass cls = owlModel.createRDFSNamedClass("Class");
        RDFSClass otherCls = owlModel.createRDFSNamedClass("OtherClass");
        int classCount = owlModel.getClsCount();
        OWLUnionClass unionClass = owlModel.createOWLUnionClass();
        unionClass.addOperand(cls);
        unionClass.addOperand(otherCls);
        property.setDomain(unionClass);
        assertEquals(unionClass, property.getDomain(false));
        property.setDomain(null);
        assertEquals(classCount, owlModel.getClsCount());
    }


    public void testSetUnionDomain() {
        RDFProperty property = owlModel.createRDFProperty("property");
        RDFSClass clsA = owlModel.createRDFSNamedClass("ClassA");
        RDFSClass clsB = owlModel.createRDFSNamedClass("ClassB");
        property.setDomain(clsA);
        assertEquals(clsA, property.getDomain(false));
        property.addUnionDomainClass(clsB);
        assertTrue(property.getDomain(false) instanceof OWLUnionClass);
        assertSize(2, ((OWLUnionClass) property.getDomain(false)).getOperands());
        property.removeUnionDomainClass(clsA);
        assertEquals(clsB, property.getDomain(false));
    }
}
