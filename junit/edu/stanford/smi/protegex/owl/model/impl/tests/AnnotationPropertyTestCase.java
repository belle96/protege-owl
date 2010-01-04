package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AnnotationPropertyTestCase extends AbstractJenaTestCase {

    public void testCreateAnnotationDatatypeProperty() {
        OWLDatatypeProperty datatypeSlot = owlModel.createAnnotationOWLDatatypeProperty("StringAnno");
        assertEquals(ValueType.ANY, ((Slot) datatypeSlot).getValueType());
        assertAnnotationProperty(datatypeSlot);
    }


    public void testCreateAnnotationObjectProperty() {
        OWLObjectProperty objectSlot = owlModel.createAnnotationOWLObjectProperty("ObjectAnno");
        assertEquals(ValueType.INSTANCE, ((Slot) objectSlot).getValueType());
        assertAnnotationProperty(objectSlot);
    }


    public void testOntologyAnnotationPropertiesHaveDomainDefined() {
        assertOntologyProperty(OWLNames.Slot.BACKWARD_COMPATIBLE_WITH);
        assertOntologyProperty(OWLNames.Slot.INCOMPATIBLE_WITH);
        assertOntologyProperty(OWLNames.Slot.PRIOR_VERSION);
    }


    public void testSetAnnotationPropertyDomain() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLDatatypeProperty anno = owlModel.createAnnotationOWLDatatypeProperty("anno");
        anno.setDomain(cls);
        assertEquals(cls, anno.getDomain(false));
        Collection domainProperties = cls.getUnionDomainProperties(true);
        assertContains(anno, domainProperties);
    }

    public void testCreateAnnotationProperty() {
        RDFProperty annProp = owlModel.createAnnotationProperty("Annotation");    
        assertAnnotationProperty(annProp);
    }
    

    private void assertAnnotationProperty(RDFProperty property) {
        assertTrue(property.isAnnotationProperty());
        assertFalse(property.isFunctional());
        assertFalse(property.isDomainDefined());
        assertEquals(owlModel.getRootClses(), property.getUnionDomain());
    }


    private void assertOntologyProperty(String slotName) {
        RDFProperty property = owlModel.getOWLBackwardCompatibleWithProperty();
        assertTrue(property.isAnnotationProperty());
        assertTrue(property.isDomainDefined());
        assertSize(1, property.getUnionDomain());
        assertEquals(owlModel.getCls(OWLNames.Cls.ONTOLOGY), property.getUnionDomain().iterator().next());
    }
}
