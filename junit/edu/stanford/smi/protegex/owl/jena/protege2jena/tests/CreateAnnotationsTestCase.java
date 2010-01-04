package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.vocabulary.OWL;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateAnnotationsTestCase extends AbstractProtege2JenaTestCase {


    public void testCreateAnnotationProperty() {
        OWLDatatypeProperty slot = owlModel.createAnnotationOWLDatatypeProperty("slot");
        OntModel newModel = createOntModel();
        OntProperty property = newModel.getOntProperty(slot.getURI());
        assertTrue(property.hasRDFType(OWL.DatatypeProperty));
        assertTrue(property.hasRDFType(OWL.AnnotationProperty));
    }


    public void testAnonymousClassAnnotations() {
        OWLNamedClass namedCls = owlModel.createOWLNamedClass("Cls");
        OWLAnonymousClass anonymousCls = owlModel.createOWLComplementClass(namedCls);
        namedCls.addSuperclass(anonymousCls);
        OWLProperty annotationOWLProperty = (OWLProperty) owlModel.getSlot(RDFSNames.Slot.COMMENT);
        anonymousCls.setPropertyValue(annotationOWLProperty, "A");
        OntModel newModel = createOntModel();
        ComplementClass anonClass = (ComplementClass) newModel.listComplementClasses().next();
        OntProperty annotationOntProperty = newModel.getOntProperty(annotationOWLProperty.getURI());
        assertHasValue(anonClass, annotationOntProperty, ValueType.STRING, "A");
    }


    public void testLabels() {
        OWLNamedClass namedCls = owlModel.createOWLNamedClass("Cls");
        namedCls.addLabel("D", "de");
        namedCls.addLabel("E", "en");
        assertContains(owlModel.createRDFSLiteral("D", "de"),
                namedCls.getPropertyValues(owlModel.getRDFSLabelProperty()));
        String uri = namedCls.getURI();
        OntModel newModel = createOntModel();
        OntClass ontClass = newModel.getOntClass(uri);
        assertEquals("D", ontClass.getLabel("de"));
        assertEquals("E", ontClass.getLabel("en"));
    }


    public void testNamedClassAnnotations() {
        OWLNamedClass namedCls = owlModel.createOWLNamedClass("Cls");
        OWLProperty annotationProperty = (OWLProperty) owlModel.getSlot(RDFSNames.Slot.COMMENT);
        namedCls.setPropertyValue(annotationProperty, "A");
        String uri = namedCls.getURI();
        OntModel newModel = createOntModel();
        OntClass ontClass = newModel.getOntClass(uri);
        OntProperty annotationOntProperty = newModel.getOntProperty(annotationProperty.getURI());
        assertHasValue(ontClass, annotationOntProperty, ValueType.STRING, "A");
    }


    public void testPropertyAnnotations() {
        OWLProperty owlProperty = owlModel.createOWLDatatypeProperty("property", owlModel.getXSDstring());
        OWLProperty annotationProperty = (OWLProperty) owlModel.getSlot(RDFSNames.Slot.COMMENT);
        owlProperty.setPropertyValue(annotationProperty, "A");
        String uri = owlProperty.getURI();
        OntModel newModel = createOntModel();
        DatatypeProperty ontProperty = newModel.getDatatypeProperty(uri);
        OntProperty annotationOntProperty = newModel.getOntProperty(annotationProperty.getURI());
        assertHasValue(ontProperty, annotationOntProperty, ValueType.STRING, "A");
    }
}
