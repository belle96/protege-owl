package edu.stanford.smi.protegex.owl.jena.graph.tests;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import edu.stanford.smi.protegex.owl.jena.graph.ProtegeGraph;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProtegeGraphTestCase extends AbstractJenaTestCase {

    public void testContainsWithSubjectPredicateObjectLiteral() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        c.addPropertyValue(owlModel.getOWLVersionInfoProperty(), "Test");
        ProtegeGraph graph = new ProtegeGraph(owlModel, owlModel.getTripleStoreModel().getActiveTripleStore());
        assertTrue(graph.contains(
                Node.createURI(c.getURI()),
                OWL.versionInfo.getNode(),
                Node.createLiteral("Test", null, XMLSchemaDatatypes.getRDFDatatype(owlModel.getXSDstring()))));
    }


    public void testContainsWithSubjectPredicateObjectResource() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        ProtegeGraph graph = new ProtegeGraph(owlModel, owlModel.getTripleStoreModel().getActiveTripleStore());
        assertTrue(graph.contains(
                Node.createURI(c.getURI()),
                RDF.type.getNode(),
                OWL.Class.getNode()));
    }


    public void testFindAllOnEmptyModel() {
        ProtegeGraph graph = new ProtegeGraph(owlModel, owlModel.getTripleStoreModel().getActiveTripleStore());
        Iterator it = graph.find(null, null, null);
        assertTrue(it.hasNext());
        Triple triple = (Triple) it.next();
        assertEquals(owlModel.getDefaultOWLOntology().getURI(), triple.getSubject().getURI());
        assertEquals(RDF.type.getURI(), triple.getPredicate().getURI());
        assertEquals(OWL.Ontology.getURI(), triple.getObject().getURI());
        assertFalse(it.hasNext());
    }


    public void testFindWithObjectOnly() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        ProtegeGraph graph = new ProtegeGraph(owlModel, owlModel.getTripleStoreModel().getActiveTripleStore());
        Iterator it = graph.find(
                null,
                null,
                OWL.Class.getNode());
        assertTrue(it.hasNext());
        Triple triple = (Triple) it.next();
        assertEquals(c.getURI(), triple.getSubject().getURI());
        assertEquals(RDF.type.getURI(), triple.getPredicate().getURI());
        assertEquals(OWL.Class.getURI(), triple.getObject().getURI());
    }


    public void testFindWithPredicateOnly() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        ProtegeGraph graph = new ProtegeGraph(owlModel, owlModel.getTripleStoreModel().getActiveTripleStore());
        Iterator it = graph.find(
                null,
                RDF.type.getNode(),
                null);
        while (it.hasNext()) {
          Triple triple = (Triple) it.next();
          if (c.getURI().equals(triple.getSubject().getURI()) && 
              RDF.type.getURI().equals(triple.getPredicate().getURI()) && 
              OWL.Class.getURI().equals(triple.getObject().getURI())) {
            return;
          }
        }
        fail();
    }


    public void testFindWithSubjectOnly() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        ProtegeGraph graph = new ProtegeGraph(owlModel, owlModel.getTripleStoreModel().getActiveTripleStore());
        Iterator it = graph.find(
                Node.createURI(c.getURI()),
                null,
                null);
        assertTrue(it.hasNext());

        Triple typeTriple = (Triple) it.next();
        assertTrue(it.hasNext());
        Triple subclassTriple = (Triple) it.next();
        assertFalse(it.hasNext());

        if (RDF.type.getURI().equals(subclassTriple.getPredicate().getURI())) {
            Triple ring = typeTriple;
            typeTriple = subclassTriple;
            subclassTriple = ring;
        }

        assertEquals(c.getURI(), typeTriple.getSubject().getURI());
        assertEquals(RDF.type.getURI(), typeTriple.getPredicate().getURI());
        assertEquals(OWL.Class.getURI(), typeTriple.getObject().getURI());
        assertEquals(c.getURI(), subclassTriple.getSubject().getURI());
        assertEquals(RDFS.subClassOf.getURI(), subclassTriple.getPredicate().getURI());
        assertEquals(OWL.Thing.getURI(), subclassTriple.getObject().getURI());

        assertEquals(3, graph.size());
    }


    public void testFindWithSubjectPredicate() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        ProtegeGraph graph = new ProtegeGraph(owlModel, owlModel.getTripleStoreModel().getActiveTripleStore());
        Iterator it = graph.find(
                Node.createURI(c.getURI()),
                RDF.type.getNode(),
                null);
        assertTrue(it.hasNext());
        Triple triple = (Triple) it.next();
        assertFalse(it.hasNext());
        assertEquals(c.getURI(), triple.getSubject().getURI());
        assertEquals(RDF.type.getURI(), triple.getPredicate().getURI());
        assertEquals(OWL.Class.getURI(), triple.getObject().getURI());
    }


    public void testFindAnonymousClass() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        OWLRestriction restriction = owlModel.createOWLMinCardinality(property, 1);
        c.addSuperclass(restriction);
        ProtegeGraph graph = new ProtegeGraph(owlModel, owlModel.getTripleStoreModel().getActiveTripleStore());
        Iterator it = graph.find(
                null,
                OWL.minCardinality.getNode(),
                null);
        assertTrue(it.hasNext());
        Triple triple = (Triple) it.next();
        assertFalse(it.hasNext());
        assertTrue(triple.getSubject().isBlank());
        assertEquals(restriction.getName(), triple.getSubject().getBlankNodeId().toString());
    }
}
