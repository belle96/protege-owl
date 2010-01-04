package edu.stanford.smi.protegex.owl.jena.parser.tests;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.search.finder.DefaultClassFind;
import edu.stanford.smi.protegex.owl.ui.search.finder.Find;
import edu.stanford.smi.protegex.owl.ui.search.finder.SearchAdapter;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadNamespacesTestCase extends AbstractJenaTestCase {

    private URI tempSavedFileURI = new File(ProtegeOWL.getPluginFolder(), "temp.owl").toURI();

    private static final String TEST_1 = "http://www.co-ode.org/ontology/test1.owl#";
    private static final String TEST_2 = "http://www.co-ode.org/ontology/test2.owl#";

    private static final String TEST_1_PREFIX = "test1";
    private static final String TEST_2_PREFIX = "test2";
    private static final String AUTO_1_PREFIX = "p1";
    private static final String AUTO_2_PREFIX = "p2";

    private boolean searching = true;

    public void testLoadOntologyWithSameDefaultNSAndXMLBase() throws Exception {
        loadRemoteOntology("namespaces/nsAndBaseSame.owl");

        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("Class_1", TEST_1);
        expectedNamespaces.put("Class_2", TEST_1);
        expectedNamespaces.put("Class_3", TEST_2);

        Map<String, String> expectedPrefixes = new HashMap<String, String>();
        //default prefix -> ""
        expectedPrefixes.put("Class_1", "");
        expectedPrefixes.put("Class_2", "");
        expectedPrefixes.put("Class_3", TEST_2_PREFIX);

        assertOntologyIsAsExpected(expectedNamespaces, expectedPrefixes);
        assertEquals("http://www.co-ode.org/ontology/test1.owl",
                     owlModel.getDefaultOWLOntology().getURI());

        owlModel.save(tempSavedFileURI);
        Log.getLogger().info("saved to " + tempSavedFileURI);

        loadTestOntology(tempSavedFileURI);

        assertOntologyIsAsExpected(expectedNamespaces, expectedPrefixes);
        assertEquals("http://www.co-ode.org/ontology/test1.owl",
                     owlModel.getDefaultOWLOntology().getURI());
    }

    public void testLoadOntologyWithDifferentDefaultNSAndXMLBase() throws Exception {
        loadRemoteOntology("namespaces/nsAndBaseDiff.owl");

        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("Class_1", TEST_1);
        expectedNamespaces.put("Class_2", TEST_1);
        expectedNamespaces.put("Class_3", TEST_2);

        Map<String, String> expectedPrefixes = new HashMap<String, String>();
        expectedPrefixes.put("Class_1", null);
        expectedPrefixes.put("Class_2", null);
        expectedPrefixes.put("Class_3", "");

        assertOntologyIsAsExpected(expectedNamespaces, expectedPrefixes);

        owlModel.save(tempSavedFileURI);
        Log.getLogger().info("saved to " + tempSavedFileURI);

        loadTestOntology(tempSavedFileURI);

        assertOntologyIsAsExpected(expectedNamespaces, expectedPrefixes);
    }

    public void testLoadOntologyWithNoDefaultNSOrXMLBase() throws Exception {
        loadRemoteOntology("namespaces/noDefaultNSNoBase.owl");

        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("Class_1", TEST_1);
        expectedNamespaces.put("Class_2", TEST_1);
        expectedNamespaces.put("Class_3", TEST_2);

        Map<String, String> expectedPrefixes = new HashMap<String, String>();
        expectedPrefixes.put("Class_1", null);
        expectedPrefixes.put("Class_2", null);
        expectedPrefixes.put("Class_3", TEST_2_PREFIX);

        assertOntologyIsAsExpected(expectedNamespaces, expectedPrefixes);
        assertEquals("http://protege.stanford.edu/junitOntologies/testset/namespaces/noDefaultNSNoBase.owl",
                     owlModel.getDefaultOWLOntology().getURI());

        owlModel.save(tempSavedFileURI);
        Log.getLogger().info("saved to " + tempSavedFileURI);

        loadTestOntology(tempSavedFileURI);

        assertOntologyIsAsExpected(expectedNamespaces, expectedPrefixes);
    }


    public void testDefaultNamespaces() throws Exception {
        Collection oldOntologies = owlModel.getOWLOntologies();
        assertSize(1, oldOntologies);
        OWLModel newModel = reload(owlModel);
        Collection ontologies = newModel.getOWLOntologies();
        assertSize(1, ontologies);
        assertContains(newModel.getDefaultOWLOntology(), ontologies);
        NamespaceManager nsm = newModel.getNamespaceManager();
        //assertEquals(ProtegeNames.DEFAULT_DEFAULT_NAMESPACE, nsm.getDefaultNamespace());
        assertEquals(OWL.getURI(), nsm.getNamespaceForPrefix("owl"));
        assertEquals(RDF.getURI(), nsm.getNamespaceForPrefix("rdf"));
        assertEquals(RDFS.getURI(), nsm.getNamespaceForPrefix("rdfs"));
    }


    public void testMissingPrefixForImport() throws Exception {
        loadRemoteOntology("importTravelNoPrefix.owl");
    }


    public void testMissingPrefix() throws Exception {
        loadRemoteOntology("concepts.owl");
        assertNotNull(owlModel.getDefaultOWLOntology());
    }

    private void assertOntologyIsAsExpected(Map<String, String> expectedNamespaces, Map<String, String> expectedPrefixes) {
        final Object lock = new Object();
        Find find = new DefaultClassFind(owlModel, Find.ENDS_WITH).getFind();
        find.addResultListener(new SearchAdapter() {
            public void searchCompleteEvent(int numResults, Find source) {
              synchronized (lock) {
                searching = false;
                lock.notifyAll();
              }
            }
        });

        for (Iterator i = expectedNamespaces.keySet().iterator(); i.hasNext();) {
            String current = (String) i.next();
            Log.getLogger().info("current = " + current);
            searching = true;
            find.startSearch(current);
            synchronized (lock) {
              while (searching) {
                try {
                  lock.wait();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                  fail();
                }
              }
            }

            Set results = find.getResultResources();
            assertTrue(results.size() > 0);
            OWLNamedClass c = (OWLNamedClass) CollectionUtilities.getFirstItem(results);
            Log.getLogger().info("current URI = " + c.getURI());
            assertNotNull(c);
            assertEquals(expectedNamespaces.get(current), c.getNamespace());
            assertEquals(expectedPrefixes.get(current), c.getNamespacePrefix());
        }

        assertNotNull(owlModel.getDefaultOWLOntology());
    }
}
