package edu.stanford.smi.protegex.owl.model.impl.tests;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultRDFSLiteralTestCase extends AbstractJenaTestCase {

    public void testInt() {
        RDFSDatatype datatype = owlModel.getXSDint();
        RDFSLiteral literal = owlModel.createRDFSLiteral("42", datatype);
        assertEquals(datatype, literal.getDatatype());
        assertEquals("42", literal.getString());
        assertEquals(42, literal.getInt());
        RDFSLiteral otherLiteral = owlModel.createRDFSLiteral("42", datatype);
        assertEquals(literal, otherLiteral);
        assertEquals(literal.hashCode(), otherLiteral.hashCode());
    }


    public void testSetAndGetLanguage() {
        RDFSDatatype datatype = owlModel.getXSDstring();
        RDFProperty property = owlModel.createAnnotationOWLDatatypeProperty("property");
        property.setRange(datatype);
        RDFResource subject = owlModel.createRDFSNamedClass("Cls");
        subject.addPropertyValue(property, "Test");
        assertEquals("Test", subject.getPropertyValue(property));
        subject.setPropertyValue(property, owlModel.createRDFSLiteralOrString("Text", "de"));
        Object value = subject.getPropertyValue(property);
        assertTrue(value instanceof RDFSLiteral);
        RDFSLiteral literal = (RDFSLiteral) value;
        assertEquals("Text", literal.getString());
        assertEquals("de", literal.getLanguage());
        assertEquals(owlModel.getXSDstring(), literal.getDatatype());
    }


    public void testSetOptimized() {
        RDFSDatatype datatype = owlModel.getXSDstring();
        RDFProperty property = owlModel.createAnnotationOWLDatatypeProperty("property");
        property.setRange(datatype);
        RDFResource subject = owlModel.createRDFSNamedClass("Cls");

        RDFSLiteral stringLiteral = owlModel.createRDFSLiteral("Text");
        subject.setPropertyValue(property, stringLiteral);
        Object stringValue = subject.getPropertyValue(property);
        assertTrue(stringValue instanceof String);
        assertEquals("Text", stringValue);

        RDFSLiteral intLiteral = owlModel.createRDFSLiteral(new Integer(42));
        subject.setPropertyValue(property, intLiteral);
        Object intValue = subject.getPropertyValue(property);
        assertTrue(intValue instanceof Integer);
        assertEquals(new Integer(42), intValue);

        RDFSLiteral uintLiteral = owlModel.createRDFSLiteral("42", owlModel.getRDFSDatatypeByURI(XSDDatatype.XSDunsignedInt.getURI()));
        subject.setPropertyValue(property, uintLiteral);
        Object uintValue = subject.getPropertyValue(property);
        assertTrue(uintValue instanceof RDFSLiteral);
        assertEquals(uintLiteral, uintValue);
    }


    public void testAnyURI() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        property.setDomain(cls);
        OWLIndividual individual = cls.createOWLIndividual("Individual");
        String uri1 = "http://aldi.de";
        String uri2 = "http://lidl.de";
        RDFSDatatype datatype = owlModel.getRDFSDatatypeByName("xsd:anyURI");
        individual.addPropertyValue(property, owlModel.createRDFSLiteral(uri1, datatype));
        individual.addPropertyValue(property, owlModel.createRDFSLiteral(uri2, datatype));
        Collection values = individual.getPropertyValues(property);
        assertSize(2, values);
        assertContains(owlModel.createRDFSLiteral(uri1, datatype), values);
        assertContains(owlModel.createRDFSLiteral(uri2, datatype), values);
        assertTrue(values.iterator().next() instanceof RDFSLiteral);
    }


    public void testDoubleIsHandledAsRDFSLiteral() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        property.setDomain(cls);
        RDFSDatatype datatype = owlModel.getRDFSDatatypeByName("xsd:double");
        assertNotNull(datatype);
        property.setRange(datatype);
        OWLIndividual individual = cls.createOWLIndividual("Individual");
        RDFSLiteral literal = owlModel.createRDFSLiteral("4.2", datatype);
        individual.setPropertyValue(property, literal);
        Object value = individual.getPropertyValue(property);
        assertEquals(value, literal);
    }


    public void testBooleanTrue() {
        RDFProperty property = owlModel.createRDFProperty("property");
        property.setRange(owlModel.getXSDboolean());
        owlThing.setPropertyValue(property, Boolean.TRUE);
        RDFSLiteral literal = owlThing.getPropertyValueLiteral(property);
        assertTrue(literal.getBoolean());
    }


    public void testWeirdBoolean() {
        final RDFSDatatype bool = owlModel.getXSDboolean();
        RDFSLiteral literal = owlModel.createRDFSLiteral("1", bool);
        assertEquals(bool, literal.getDatatype());
        assertTrue(literal.getBoolean());
    }


    public void testIntWithWhitespaces() {
        String raw = "   \n\t\t10 \n";
        RDFSLiteral literal = owlModel.createRDFSLiteral(raw, owlModel.getXSDint());
        assertEquals(10, literal.getInt());
    }


    public void testInvalidPlainValues() {

        RDFSLiteral intLiteral = owlModel.createRDFSLiteral("", owlModel.getXSDint());
        assertEquals(new Integer(0), intLiteral.getPlainValue());

        RDFSLiteral floatLiteral = owlModel.createRDFSLiteral("", owlModel.getXSDfloat());
        assertEquals(new Float(0), floatLiteral.getPlainValue());

        RDFSLiteral booleanLiteral = owlModel.createRDFSLiteral("", owlModel.getXSDboolean());
        assertEquals(Boolean.FALSE, booleanLiteral.getPlainValue());
    }


    public void testCreateLiteralWithAnonymousDatatype() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        RDFSDatatype datatype = owlModel.createRDFSDatatype(owlModel.getNextAnonymousResourceName());
        RDFProperty subDatatypeOfProperty = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_BASE);
        datatype.setPropertyValue(subDatatypeOfProperty, owlModel.getXSDint());
        RDFSLiteral literal = owlModel.createRDFSLiteral("10", datatype);
        assertEquals(owlModel.getXSDint(), literal.getDatatype());
    }


    public void testCompareFloats() {
        RDFSLiteral a = owlModel.createRDFSLiteral("2.1", owlModel.getXSDfloat());
        RDFSLiteral b = owlModel.createRDFSLiteral("4.3", owlModel.getXSDfloat());
        assertTrue(a.compareTo(b) < 0);
        assertTrue(a.compareTo(a) == 0);
        assertTrue(b.compareTo(a) > 0);
    }


    public void testCompareInts() {
        RDFSLiteral a = owlModel.createRDFSLiteral("2", owlModel.getXSDinteger());
        RDFSLiteral b = owlModel.createRDFSLiteral("4", owlModel.getXSDinteger());
        assertTrue(a.compareTo(b) < 0);
        assertTrue(a.compareTo(a) == 0);
        assertTrue(b.compareTo(a) > 0);
    }
}
