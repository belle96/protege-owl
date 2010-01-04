package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.vocabulary.OWL;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateAnnotationsTestCase extends AbstractJenaCreatorTestCase {


    public void testCreateAnnotationProperty() {
        OWLDatatypeProperty slot = owlModel.createAnnotationOWLDatatypeProperty("slot");
        OntModel newModel = runJenaCreator();
        OntProperty property = newModel.getOntProperty(slot.getURI());
        assertTrue(property.hasRDFType(OWL.DatatypeProperty));
        assertTrue(property.hasRDFType(OWL.AnnotationProperty));
    }


    public void testAnonymousClassAnnotations() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Cls");
        OWLAnonymousClass anonymousClass = owlModel.createOWLComplementClass(namedClass);
        namedClass.addSuperclass(anonymousClass);
        OWLProperty annotationOWLProperty = owlModel.getRDFSCommentProperty();
        anonymousClass.setPropertyValue(annotationOWLProperty, "A");
        OntModel newModel = runJenaCreator();
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
        OntModel newModel = runJenaCreator();
        OntClass ontClass = newModel.getOntClass(uri);
        assertEquals("D", ontClass.getLabel("de"));
        assertEquals("E", ontClass.getLabel("en"));
    }


    public void testNamedClassAnnotations() {
        OWLNamedClass namedCls = owlModel.createOWLNamedClass("Cls");
        OWLProperty annotationProperty = owlModel.getRDFSCommentProperty();
        namedCls.setPropertyValue(annotationProperty, "A");
        String uri = namedCls.getURI();
        OntModel newModel = runJenaCreator();
        OntClass ontClass = newModel.getOntClass(uri);
        OntProperty annotationOntProperty = newModel.getOntProperty(annotationProperty.getURI());
        assertHasValue(ontClass, annotationOntProperty, ValueType.STRING, "A");
    }


    public void testPropertyAnnotations() {
        OWLProperty owlProperty = owlModel.createOWLDatatypeProperty("property", owlModel.getXSDstring());
        OWLProperty annotationProperty = owlModel.getRDFSCommentProperty();
        owlProperty.setPropertyValue(annotationProperty, "A");
        String uri = owlProperty.getURI();
        OntModel newModel = runJenaCreator();
        DatatypeProperty ontProperty = newModel.getDatatypeProperty(uri);
        OntProperty annotationOntProperty = newModel.getOntProperty(annotationProperty.getURI());
        assertHasValue(ontProperty, annotationOntProperty, ValueType.STRING, "A");
    }
}
