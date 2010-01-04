package edu.stanford.smi.protegex.owl.jena.triplestore.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.triplestore.Triple;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.model.triplestore.impl.DefaultTriple;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */



public class ListTriplesTestCase extends AbstractJenaTestCase {

	public static final int DEFAULT_SIZE = 1;

    public void testDefaultTriples() {
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        assertSize(DEFAULT_SIZE, ts.listTriples());
        Triple triple = (Triple) ts.listTriples().next();
        assertEquals(owlModel.getDefaultOWLOntology(), triple.getSubject());
        assertEquals(owlModel.getRDFTypeProperty(), triple.getPredicate());
        assertEquals(owlModel.getOWLOntologyClass(), triple.getObject());
    }


    public void testOWLNamedClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        assertSize(DEFAULT_SIZE + 2, ts.listTriples());
        assertTrue(ts.contains(cls, owlModel.getRDFTypeProperty(), owlModel.getOWLNamedClassClass()));
        assertTrue(ts.contains(cls, owlModel.getRDFSSubClassOfProperty(), owlModel.getOWLThingClass()));
    }


    public void testClassificationResultsSuppressed() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        cls.setClassificationStatus(OWLNames.CLASSIFICATION_STATUS_INCONSISTENT);
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        assertSize(DEFAULT_SIZE + 2, ts.listTriples());
        assertTrue(ts.contains(cls, owlModel.getRDFTypeProperty(), owlModel.getOWLNamedClassClass()));
        assertTrue(ts.contains(cls, owlModel.getRDFSSubClassOfProperty(), owlModel.getOWLThingClass()));
        assertTrue(ts.contains(owlModel.getDefaultOWLOntology(), owlModel.getRDFTypeProperty(), owlModel.getOWLOntologyClass()));
    }


    public void testAddSuperclassRelationship() throws Exception {
        loadRemoteOntology("importTravel.owl");
        OWLNamedClass sup = owlModel.getOWLNamedClass("travel:Contact");
        OWLNamedClass sub = owlModel.getOWLNamedClass("travel:Activity");
        sub.addSuperclass(sup);
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        Iterator<Triple> triples = ts.listTriples();
        assertTrue(ts.contains(sub, owlModel.getRDFSSubClassOfProperty(), sup));
    }


    public void testListTriplesWithObject() {
        RDFProperty property = owlModel.createRDFProperty("property");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        cls.addPropertyValue(property, cls);
        Iterator it = owlModel.getTripleStoreModel().getActiveTripleStore().listTriplesWithObject(cls);
        assertEquals(new DefaultTriple(cls, property, cls), it.next());
        assertFalse(it.hasNext());
    }
}
