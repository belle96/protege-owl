package edu.stanford.smi.protegex.owl.jena.triplestore.tests;

import edu.stanford.smi.protege.model.framestore.InMemoryFrameDb;
import edu.stanford.smi.protege.model.framestore.NarrowFrameStore;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStoreModel;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class HomeTripleStoreTestCase extends AbstractJenaTestCase {

    public void testGetHomeTripleStore() {
        RDFResource resource = owlModel.createOWLNamedClass("Class");
        TripleStoreModel tripleStoreModel = owlModel.getTripleStoreModel();
        assertEquals(tripleStoreModel.getActiveTripleStore(), tripleStoreModel.getHomeTripleStore(resource));
    }


    public void testSetHomeTripleStore() {
        RDFResource resource = owlModel.createOWLNamedClass("Class");
        TripleStoreModel tripleStoreModel = owlModel.getTripleStoreModel();
        TripleStore activeTripleStore = tripleStoreModel.getActiveTripleStore();
        assertEquals(activeTripleStore, tripleStoreModel.getHomeTripleStore(resource));
        NarrowFrameStore nfs = new InMemoryFrameDb("Test");
        TripleStore newTripleStore = tripleStoreModel.createActiveImportedTripleStore(nfs);
        tripleStoreModel.setActiveTripleStore(activeTripleStore);
        tripleStoreModel.setHomeTripleStore(resource, newTripleStore);
        assertEquals(newTripleStore, tripleStoreModel.getHomeTripleStore(resource));
    }
}
