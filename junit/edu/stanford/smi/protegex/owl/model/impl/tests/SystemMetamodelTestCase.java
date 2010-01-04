package edu.stanford.smi.protegex.owl.model.impl.tests;

import java.util.Collection;
import java.util.Iterator;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLModel;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SystemMetamodelTestCase extends AbstractJenaTestCase {

    public void testSystemFrames() {
        Collection ss = ((AbstractOWLModel) owlModel).getOWLSystemResources();

        for (Iterator it = ss.iterator(); it.hasNext();) {
            Frame frame = (Frame) it.next();
            assertTrue(frame.isSystem());
            assertFalse(frame.isEditable());
        }

        OWLNamedClass cls = owlModel.createOWLNamedClass("Test");
        assertFalse(cls.isSystem());
    }


    public void testJavatypeOfSystemClasses() {
        assertTrue(owlModel.getCls(RDFSNames.Cls.NAMED_CLASS) instanceof RDFSNamedClass);
        assertTrue(owlModel.getCls(OWLNames.Cls.NAMED_CLASS) instanceof OWLNamedClass);
    }


    public void testOWLDeprecatedClass() {
        RDFSNamedClass c = owlModel.getRDFSNamedClass(OWLNames.Cls.DEPRECATED_CLASS);
        assertSize(1, c.getSuperclasses(false));
        assertEquals(owlModel.getRDFSNamedClassClass(), c.getFirstSuperclass());
    }


    public void testOWLThing() {
        RDFProperty rdfTypeProperty = rdfTypeProperty();
        assertSize(1, owlThing.getPropertyValues(rdfTypeProperty));
        assertEquals(owlModel.getOWLNamedClassClass(), owlThing.getPropertyValue(rdfTypeProperty));
    }


    private RDFProperty rdfTypeProperty() {
        return owlModel.getRDFTypeProperty();
    }


    public void testUserDefinedNamedClasses() {
        assertSize(0, owlModel.getUserDefinedOWLNamedClasses());
        assertSize(0, owlModel.getUserDefinedRDFSNamedClasses());
        RDFSNamedClass c = owlModel.createRDFSNamedClass("Class");
        assertSize(0, owlModel.getUserDefinedOWLNamedClasses());
        assertSize(1, owlModel.getUserDefinedRDFSNamedClasses());
        assertContains(c, owlModel.getUserDefinedRDFSNamedClasses());
    }


    public void testUserDefinedProperties() {
        assertSize(0, owlModel.getUserDefinedOWLProperties());
        assertSize(0, owlModel.getUserDefinedRDFProperties());
        assertSize(0, owlModel.getVisibleUserDefinedOWLProperties());
        assertSize(0, owlModel.getVisibleUserDefinedRDFProperties());
        RDFProperty property = owlModel.createRDFProperty("property");
        assertSize(0, owlModel.getUserDefinedOWLProperties());
        assertSize(1, owlModel.getUserDefinedRDFProperties());
        assertSize(0, owlModel.getVisibleUserDefinedOWLProperties());
        assertSize(1, owlModel.getVisibleUserDefinedRDFProperties());
        property.setVisible(false);
        assertSize(1, owlModel.getUserDefinedRDFProperties());
        assertSize(0, owlModel.getVisibleUserDefinedRDFProperties());
    }


    public void testRanges() {
        assertEquals(ValueType.STRING, ((Slot) owlModel.getRDFSLabelProperty()).getValueType());
        assertEquals(owlModel.getXSDNonNegativeInteger(), owlModel.getRDFProperty(OWLNames.Slot.MAX_CARDINALITY).getRange());
        assertEquals(owlModel.getXSDNonNegativeInteger(), owlModel.getRDFProperty(OWLNames.Slot.MIN_CARDINALITY).getRange());
        assertEquals(owlModel.getXSDNonNegativeInteger(), owlModel.getRDFProperty(OWLNames.Slot.CARDINALITY).getRange());
        assertEquals(owlModel.getRDFSNamedClassClass(), owlModel.getRDFSSubClassOfProperty().getRange());
    }


    public void testRDFSSubPropertyOf() {
        Frame frame = owlModel.getFrame(RDFSNames.Slot.SUB_PROPERTY_OF);
        assertTrue(frame instanceof RDFProperty);
    }


    public void testRDFTypeProperty() {
        RDFProperty property = owlModel.getRDFTypeProperty();
        assertFalse(property.isDomainDefined());
        assertSize(1, ((Slot) property).getDirectDomain());
        assertContains(owlThing, ((Slot) property).getDirectDomain());
    }


    public void testOWLSameAs() {
        RDFProperty property = owlModel.getOWLSameAsProperty();
        assertEquals(OWLNames.Slot.SAME_AS, property.getName());
        assertTrue(property.hasObjectRange());
    }
}
