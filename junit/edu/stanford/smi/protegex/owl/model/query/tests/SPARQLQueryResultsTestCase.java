package edu.stanford.smi.protegex.owl.model.query.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.query.QueryResults;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Map;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SPARQLQueryResultsTestCase extends AbstractJenaTestCase {

    public void testSimpleQuery() throws Exception {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        String query = "SELECT ?subject WHERE { ?subject rdf:type owl:Class }";
        QueryResults results = owlModel.executeSPARQLQuery(query);
        assertTrue(results.hasNext());
        assertSize(1, results.getVariables());
        Object var = results.getVariables().get(0);
        assertEquals("subject", var);
        Map map = results.next();
        assertFalse(results.hasNext());
        assertEquals(1, map.size());
        assertEquals(c, map.get(var));
    }


    public void testXSDIntegerQueryFindObject() throws Exception {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        RDFProperty property = owlModel.createOWLDatatypeProperty("hasAge");
        RDFResource individual = cls.createInstance("Klaus");
        RDFSLiteral literal = owlModel.createRDFSLiteral("18", owlModel.getXSDinteger());
        individual.setPropertyValue(property, literal);
        assertEquals(literal, individual.getPropertyValue(property));
        String query = "SELECT ?object WHERE { ?subject :hasAge ?object }";
        QueryResults results = owlModel.executeSPARQLQuery(query);
        assertTrue(results.hasNext());
        Object var = results.getVariables().get(0);
        Map map = results.next();
        assertFalse(results.hasNext());
        assertEquals(literal, map.get(var));
    }


    public void testXSDIntegerQueryFindSubject() throws Exception {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        RDFProperty property = owlModel.createOWLDatatypeProperty("hasAge");
        RDFResource individual = cls.createInstance("Klaus");
        RDFSLiteral literal = owlModel.createRDFSLiteral("18", owlModel.getXSDinteger());
        individual.setPropertyValue(property, literal);
        assertEquals(literal, individual.getPropertyValue(property));
        QueryResults results = owlModel.executeSPARQLQuery("SELECT ?subject WHERE { ?subject :hasAge 18 }");
        assertTrue(results.hasNext());
        Map map = results.next();
        assertFalse(results.hasNext());
        assertEquals(individual, map.get(results.getVariables().get(0)));
    }


    public void testXSDIntQueryFindSubject() throws Exception {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        RDFProperty property = owlModel.createOWLDatatypeProperty("hasAge");
        RDFResource individual = cls.createInstance("Klaus");
        RDFSLiteral literal = owlModel.createRDFSLiteral("18", owlModel.getXSDint());
        individual.setPropertyValue(property, literal);
        assertEquals(new Integer(18), individual.getPropertyValue(property));
        QueryResults results = owlModel.executeSPARQLQuery("SELECT ?subject WHERE { ?subject :hasAge \"18\"^^xsd:int }");
        assertTrue(results.hasNext());
        Map map = results.next();
        assertFalse(results.hasNext());
        assertEquals(individual, map.get(results.getVariables().get(0)));
    }


    public void testXSDIntegerQueryFindSubjectAndPredicate() throws Exception {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        RDFProperty property = owlModel.createOWLDatatypeProperty("hasAge");
        RDFResource individual = cls.createInstance("Klaus");
        RDFSLiteral literal = owlModel.createRDFSLiteral("18", owlModel.getXSDinteger());
        individual.setPropertyValue(property, literal);
        QueryResults results = owlModel.executeSPARQLQuery("SELECT ?subject WHERE { ?subject ?predicate 18 }");
        assertTrue(results.hasNext());
        Map map = results.next();
        assertFalse(results.hasNext());
        assertEquals(individual, map.get("subject"));
    }


    public void testXSDIntQueryFindSubjectAndPredicate() throws Exception {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        RDFProperty property = owlModel.createOWLDatatypeProperty("hasAge");
        RDFResource individual = cls.createInstance("Klaus");
        RDFSLiteral literal = owlModel.createRDFSLiteral("18", owlModel.getXSDint());
        individual.setPropertyValue(property, literal);
        QueryResults results = owlModel.executeSPARQLQuery("SELECT ?subject WHERE { ?subject ?predicate \"18\"^^xsd:int }");
        assertTrue(results.hasNext());
        Map map = results.next();
        assertFalse(results.hasNext());
        assertEquals(individual, map.get("subject"));
    }
}
