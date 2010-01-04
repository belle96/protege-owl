package edu.stanford.smi.protegex.owl.model.patcher.tests;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLModelPatcherFailedTestCase extends AbstractJenaTestCase {

    private final static String IMPORT = "http://www.coop.de/ontology";


    public void testRDFSDomain() throws Exception {
        loadRemoteOntology("untypedRDFSDomain.owl");
        RDFProperty property = owlModel.getRDFProperty("property");
        assertNotNull(property);
        RDFResource c = owlModel.getRDFResource("ns:Class");
        assertTrue(c instanceof RDFSNamedClass);
        assertSize(1, owlModel.getTripleStoreModel().listUserTripleStores());
        TripleStore importedTS = null;
        for (TripleStore ots : owlModel.getTripleStoreModel().getTripleStores()) {
            if (!(ots.equals(owlModel.getTripleStoreModel().getSystemTripleStore())) && 
                    !(ots.equals(owlModel.getTripleStoreModel().getTopTripleStore()))) {
                importedTS = ots;
                break;
            }
        }
        assertEquals(IMPORT, importedTS.getName());
        assertTrue(importedTS.contains(c, owlModel.getRDFTypeProperty(), owlModel.getRDFSNamedClassClass()));
    }


    public void testRDFSRange() throws Exception {
        loadRemoteOntology("untypedRDFSRange.owl");
        RDFProperty property = owlModel.getRDFProperty("property");
        assertNotNull(property);
        RDFResource c = owlModel.getRDFResource("ns:Class");
        assertTrue(c instanceof RDFSNamedClass);
        assertSize(1, owlModel.getTripleStoreModel().listUserTripleStores());
        TripleStore importedTS = null;
        for (TripleStore ts : owlModel.getTripleStoreModel().getTripleStores()) {
            if (!(ts.equals(owlModel.getTripleStoreModel().getSystemTripleStore())) && 
                    !(ts.equals(owlModel.getTripleStoreModel().getTopTripleStore()))) {
                importedTS = ts;
                break;
            }
        }
        assertEquals(IMPORT, importedTS.getName());
        assertTrue(importedTS.contains(c, owlModel.getRDFTypeProperty(), owlModel.getRDFSNamedClassClass()));
    }


    public void testRDFSSubClassOf() throws Exception {
        loadRemoteOntology("untypedRDFSSubClassOf.owl");
        RDFSNamedClass subclass = owlModel.getRDFSNamedClass("Class");
        assertNotNull(subclass);
        RDFResource c = owlModel.getRDFResource("ns:Class");
        assertTrue(c instanceof RDFSNamedClass);
        assertSize(1, owlModel.getTripleStoreModel().listUserTripleStores());
        TripleStore importedTS = null;
        for (TripleStore ts : owlModel.getTripleStoreModel().getTripleStores()) {
            if (!(ts.equals(owlModel.getTripleStoreModel().getSystemTripleStore())) && 
                    !(ts.equals(owlModel.getTripleStoreModel().getTopTripleStore()))) {
                importedTS = ts;
                break;
            }
        }
        assertEquals(IMPORT, importedTS.getName());
        assertTrue(importedTS.contains(c, owlModel.getRDFTypeProperty(), owlModel.getRDFSNamedClassClass()));
        RDFSNamedClass superclass = (RDFSNamedClass) c;
        assertSize(1, superclass.getSuperclasses(false));
        assertContains(owlThing, superclass.getSuperclasses(false));
    }


    public void testUntypedPredicate() throws Exception {
        loadRemoteOntology("untypedPredicate.owl");
        RDFSNamedClass c = owlModel.getRDFSNamedClass("Class");
        assertNotNull(c);
        RDFResource property = owlModel.getRDFResource("ns:property");
        assertTrue(property instanceof RDFProperty);
        assertSize(1, owlModel.getTripleStoreModel().listUserTripleStores());
        TripleStore importedTS = null;
        for (TripleStore ts : owlModel.getTripleStoreModel().getTripleStores()) {
            if (!(ts.equals(owlModel.getTripleStoreModel().getSystemTripleStore())) && 
                    !(ts.equals(owlModel.getTripleStoreModel().getTopTripleStore()))) {
                importedTS = ts;
                break;
            }
        }
        assertEquals(IMPORT, importedTS.getName());
        assertTrue(importedTS.contains(property, owlModel.getRDFTypeProperty(), owlModel.getRDFPropertyClass()));
    }
}
