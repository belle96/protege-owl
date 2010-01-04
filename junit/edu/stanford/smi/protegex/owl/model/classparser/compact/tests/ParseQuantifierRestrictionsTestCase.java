package edu.stanford.smi.protegex.owl.model.classparser.compact.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.compact.CompactOWLClassParser;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ParseQuantifierRestrictionsTestCase extends AbstractJenaTestCase {

    private OWLClassParser parser = new CompactOWLClassParser();


    public void testParseOWLAllValuesFromWithOWLDatatypeProperty() throws Exception {
        RDFProperty property = owlModel.createOWLDatatypeProperty("property");
        RDFSClass result = parser.parseClass(owlModel, "property * double");
        assertTrue(result instanceof OWLAllValuesFrom);
        OWLAllValuesFrom allValuesFrom = (OWLAllValuesFrom) result;
        assertEquals(property, allValuesFrom.getOnProperty());
        assertEquals(owlModel.getXSDdouble(), allValuesFrom.getAllValuesFrom());
        try {
            parser.parseClass(owlModel, "property * dou");
            assertTrue(false);
        }
        catch (Exception ex) {
        }
    }


    public void testFailParseRestrictionsWithDatatypeProperty() {
        owlModel.createOWLDatatypeProperty("children", owlModel.getXSDstring());
        owlModel.createOWLNamedClass("RichPerson");
        try {
            parser.parseClass(owlModel, "children * RichPerson");
            assertTrue("Expected parser to fail", false);
        }
        catch (Exception ex) {
            // Ok. Expected
        }
        try {
            parser.parseClass(owlModel, "children ? RichPerson");
            assertTrue("Expected parser to fail", false);
        }
        catch (Exception ex) {
            // Ok. Expected
        }
    }


    public void testFailParseRestrictionsWithObjectProperty() {
        owlModel.createOWLObjectProperty("property");
        try {
            parser.parseClass(owlModel, "property ? xsd:int");
            assertTrue("Expected parser to fail", false);
        }
        catch (Exception ex) {
            // Ok. Expected
        }
        try {
            parser.parseClass(owlModel, "property ? xsd:int");
            assertTrue("Expected parser to fail", false);
        }
        catch (Exception ex) {
            // Ok. Expected
        }
    }


    public void testParseAllRestriction() throws Exception {
        String defaultNamespace = owlModel.getNamespaceManager().getDefaultNamespace();
        owlModel.createOWLObjectProperty("children");
        owlModel.createOWLNamedClass("RichPerson");
        String expression = "children * RichPerson";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLAllValuesFrom);
        OWLAllValuesFrom restriction = (OWLAllValuesFrom) aClass;
        assertTrue(restriction.isDefined());
        assertTrue(restriction.getOnProperty().getName().equals(defaultNamespace + "children"));
        assertTrue(restriction.getFiller().getName().equals(defaultNamespace + "RichPerson"));
    }


    public void testParseAllRestrictionDataType() throws Exception {
        String defaultNamespace = owlModel.getNamespaceManager().getDefaultNamespace();
        owlModel.createOWLDatatypeProperty("property", owlModel.getXSDstring());
        String expression = "property * int";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLAllValuesFrom);
        OWLAllValuesFrom restriction = (OWLAllValuesFrom) aClass;
        assertTrue(restriction.isDefined());
        assertTrue(restriction.getOnProperty().getName().equals(defaultNamespace + "property"));
        assertEquals(restriction.getFiller(), owlModel.getXSDint());
    }


    public void testParseAllRestrictionDataTypeAbbrev() throws Exception {
        String defaultNamespace = owlModel.getNamespaceManager().getDefaultNamespace();
        owlModel.createOWLDatatypeProperty("property", owlModel.getXSDstring());
        String expression = "property * int";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLAllValuesFrom);
        OWLAllValuesFrom restriction = (OWLAllValuesFrom) aClass;
        assertTrue(restriction.isDefined());
        assertTrue(restriction.getOnProperty().getName().equals(defaultNamespace + "property"));
        assertEquals(restriction.getFiller(), owlModel.getXSDint());
    }


    public void testParseSomeValuesFromWithNamedClass() throws Exception {
        String defaultNamespace = owlModel.getNamespaceManager().getDefaultNamespace();
        owlModel.createOWLNamedClass("RichPerson");
        owlModel.createOWLObjectProperty("children");
        String expression = "children ? RichPerson";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLSomeValuesFrom);
        OWLSomeValuesFrom restriction = (OWLSomeValuesFrom) aClass;
        assertTrue(restriction.isDefined());
        assertTrue(restriction.getOnProperty().getName().equals(defaultNamespace + "children"));
        assertTrue(restriction.getFiller().getName().equals(defaultNamespace + "RichPerson"));
    }


    public void testParseSomeValuesFromWithAnonymousClass() throws Exception {
        String defaultNamespace = owlModel.getNamespaceManager().getDefaultNamespace();
        owlModel.createOWLNamedClass("RichPerson");
        owlModel.createOWLObjectProperty("children");
        String expression = "children ? !RichPerson";
        parser.checkClass(owlModel, expression);
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLSomeValuesFrom);
        OWLSomeValuesFrom restriction = (OWLSomeValuesFrom) aClass;
        assertTrue(restriction.isDefined());
        assertTrue(restriction.getOnProperty().getName().equals(defaultNamespace + "children"));
        assertTrue(restriction.getFiller() instanceof OWLComplementClass);
    }
}
