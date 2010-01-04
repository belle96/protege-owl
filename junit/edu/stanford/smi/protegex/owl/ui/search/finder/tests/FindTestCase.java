package edu.stanford.smi.protegex.owl.ui.search.finder.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromUriCreator;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.search.finder.DefaultClassFind;
import edu.stanford.smi.protegex.owl.ui.search.finder.Find;
import edu.stanford.smi.protegex.owl.ui.search.finder.FindResult;
import edu.stanford.smi.protegex.owl.ui.search.finder.SearchAdapter;
import edu.stanford.smi.protegex.owl.ui.search.finder.SearchListener;
import edu.stanford.smi.protegex.owl.ui.search.finder.ThreadedFind;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         27-Jan-2006
 */
public class FindTestCase extends AbstractJenaTestCase {

    boolean complete = false;
    Find findAlg;

    SearchListener searchResultListener = new SearchAdapter() {
        public void resultsUpdatedEvent(int numResults, Find source) {
        }

        public void searchCompleteEvent(int numResults, Find source) {
            complete = true;
        }

        public void searchCancelledEvent(Find source) {
            fail();
            complete = true;
        }

        public void searchStartedEvent(Find source) {
        }
    };

    public void testThreadedSpeed() {
        loadPizza();

        Cls metaCls = owlModel.getOWLNamedClassMetaClassCls();
        metaCls.setDirectBrowserSlot(owlModel.getRDFProperty(RDFSNames.Slot.LABEL));
        findAlg = new ThreadedFind(owlModel, Find.CONTAINS);

        findAlg.addResultListener(searchResultListener);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            complete = false;
            findAlg.startSearch("p");
            while (!complete) {
                Thread.yield();
            }
        }

        long timeTaken = System.currentTimeMillis() - startTime;
        Log.getLogger().info("100 searches took " + timeTaken + "ms");
    }

    public void testFindDoesNotPickUpLanguageTags() {
        loadPizza();
        Cls metaCls = owlModel.getOWLNamedClassMetaClassCls();
        metaCls.setDirectBrowserSlot(owlModel.getRDFProperty(RDFSNames.Slot.LABEL));
        findAlg = new DefaultClassFind(owlModel, Find.CONTAINS).getFind();

        findAlg.addResultListener(searchResultListener);

        complete = false;
        findAlg.startSearch("p");
        while (!complete) {
            Thread.yield();
        }

        Map results = findAlg.getResults();
        Log.getLogger().info("results = " + results);
        assertTrue(results.size() > 0);
        for (Iterator i = results.values().iterator(); i.hasNext();) {
            FindResult result = (FindResult) i.next();
            assertNotNull(result.getMatchValue());
            assertTrue(Pattern.matches(".*[pP].*", result.getMatchValue()));
            assertTrue(result.getMatchingResource() instanceof RDFSNamedClass);
        }
    }

    private void loadPizza() {
        try {
            java.util.Collection errors = new ArrayList();
            OwlProjectFromUriCreator creator = new OwlProjectFromUriCreator();
            creator.setOntologyUri("http://www.co-ode.org/ontologies/pizza/2005/10/18/pizza.owl");
            creator.create(errors);
            if (!errors.isEmpty()) {
                fail();
            }
        }
        catch (OntologyLoadException ioe) {
            fail(ioe.getMessage());
        }
    }
}
