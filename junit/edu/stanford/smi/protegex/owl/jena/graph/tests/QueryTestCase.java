package edu.stanford.smi.protegex.owl.jena.graph.tests;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import edu.stanford.smi.protegex.owl.jena.graph.JenaModelFactory;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class QueryTestCase extends AbstractJenaTestCase {

    public void testLowLevelQuery() throws Exception {
        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
        RDFProperty property = owlModel.createOWLDatatypeProperty("hasName");
        RDFResource instance = personClass.createInstance("instance");
        instance.addPropertyValue(property, "Test");
        String queryString =
                "PREFIX : <" + owlModel.getNamespaceManager().getDefaultNamespace() + ">" +
                        "SELECT ?person ?name WHERE { ?person :hasName ?name }";
        Query query = QueryFactory.create(queryString);
        Model model = JenaModelFactory.createModel(owlModel);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        assertTrue(results.hasNext());
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            RDFNode personNode = soln.get("person");
            RDFNode nameNode = soln.get("name");
            assertEquals(instance.getURI(), personNode.toString());
            assertEquals("Test^^" + owlModel.getXSDstring().getURI(), nameNode.toString());
        }
        assertFalse(results.hasNext());
        qexec.close();
    }


    public void testLowLevelStringQuery() throws Exception {
        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
        RDFProperty property = owlModel.createOWLDatatypeProperty("hasName");
        RDFResource instance = personClass.createInstance("instance");
        instance.addPropertyValue(property, "Test");
        String queryString =
                "PREFIX : <" + owlModel.getNamespaceManager().getDefaultNamespace() + ">" +
                        "SELECT ?person ?name WHERE { ?person :hasName \"Test\" }";
        Query query = QueryFactory.create(queryString);
        Model model = JenaModelFactory.createModel(owlModel);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        assertTrue(results.hasNext());
        QuerySolution soln = results.nextSolution();
        RDFNode personNode = soln.get("person");
        assertEquals(instance.getURI(), personNode.toString());
        assertFalse(results.hasNext());
        qexec.close();
    }
}
