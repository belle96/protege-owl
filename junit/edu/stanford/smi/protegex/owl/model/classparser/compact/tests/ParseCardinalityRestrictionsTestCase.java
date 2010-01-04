package edu.stanford.smi.protegex.owl.model.classparser.compact.tests;

import edu.stanford.smi.protegex.owl.model.OWLCardinality;
import edu.stanford.smi.protegex.owl.model.OWLMaxCardinality;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.compact.CompactOWLClassParser;
import edu.stanford.smi.protegex.owl.tests.AbstractOWLModelTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ParseCardinalityRestrictionsTestCase extends AbstractOWLModelTestCase {

    private OWLClassParser parser = new CompactOWLClassParser();


    public void testParseCardiRestriction() throws Exception {
        owlModel.createOWLObjectProperty("children");
        String expression = "children = 4";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLCardinality);
        OWLCardinality restriction = (OWLCardinality) aClass;
        assertTrue(restriction.isDefined());
        assertTrue(restriction.getOnProperty().getName().equals("children"));
        assertTrue(restriction.getCardinality() == 4);
    }


    public void testParseMaxCardiRestriction() throws Exception {
        owlModel.createOWLObjectProperty("children");
        String expression = "children < 4";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLMaxCardinality);
        OWLMaxCardinality restriction = (OWLMaxCardinality) aClass;
        assertTrue(restriction.isDefined());
        assertTrue(restriction.getOnProperty().getName().equals("children"));
        assertTrue(restriction.getCardinality() == 4);
    }


    public void testParseMinCardiRestriction() throws Exception {
        owlModel.createOWLObjectProperty("children");
        String expression = "children > 4";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLMinCardinality);
        OWLMinCardinality restriction = (OWLMinCardinality) aClass;
        assertTrue(restriction.isDefined());
        assertTrue(restriction.getOnProperty().getName().equals("children"));
        assertTrue(restriction.getCardinality() == 4);
    }


    public void testParseCardiRestrictionWithNegativeCardinality() throws Exception {
        owlModel.createOWLObjectProperty("hasChildren");
        try {
            parser.parseClass(owlModel, "hasChildren = -1");
            assertTrue(false);
        }
        catch (Exception ex) {
        }
    }


    public void testParseQCardinalityRestriction() throws Exception {
        owlModel.createOWLObjectProperty("property");
        String expression = "property > 4 owl:Thing";
        RDFSClass cls = parser.parseClass(owlModel, expression);
        assertTrue(cls instanceof OWLMinCardinality);
        OWLMinCardinality restriction = (OWLMinCardinality) cls;
        assertTrue(restriction.isDefined());
        assertTrue(restriction.getOnProperty().getName().equals("property"));
        assertTrue(restriction.getCardinality() == 4);
        assertTrue(restriction.isQualified());
        assertEquals(owlModel.getOWLThingClass(), restriction.getValuesFrom());
    }
}
