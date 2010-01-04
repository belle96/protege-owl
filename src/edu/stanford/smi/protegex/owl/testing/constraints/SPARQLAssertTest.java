package edu.stanford.smi.protegex.owl.testing.constraints;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.query.QueryResults;
import edu.stanford.smi.protegex.owl.testing.*;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.query.SPARQLOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.query.SPARQLResultsPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SPARQLAssertTest extends AbstractOWLTest implements RDFSClassTest, RepairableOWLTest {

    public final static String PREFIX = "assert";

    public final static String URI = "http://www.owl-ontologies.com/assert.owl";

    public final static String NAMESPACE = URI + "#";

    public final static String EMPTY_PROPERTY_URI = NAMESPACE + "empty";

    public final static String NOT_EMPTY_PROPERTY_URI = NAMESPACE + "notEmpty";


    public SPARQLAssertTest() {
        super("Constraints", "SPARQL Asserts");
    }


    public boolean repair(OWLTestResult testResult) {
        SPARQLResultsPanel resultsPanel = SPARQLOWLModelAction.show(testResult.getHost().getOWLModel(), true);
        String queryText = (String) testResult.getUserObject();
        resultsPanel.setQueryText(queryText);
        resultsPanel.executeQuery(queryText);
        return false;
    }


    public List test(RDFSClass aClass) {
        OWLModel owlModel = aClass.getOWLModel();
        String matchesName = owlModel.getResourceNameForURI(NOT_EMPTY_PROPERTY_URI);
        if (matchesName != null) {
            RDFProperty assertMatchesProperty = owlModel.getRDFProperty(matchesName);
            if (assertMatchesProperty != null) {
                List results = new ArrayList();
                test(aClass, assertMatchesProperty, true, "at least one match", OWLIcons.ASSERT_TRUE, results);
                String noMatchesName = owlModel.getResourceNameForURI(EMPTY_PROPERTY_URI);
                RDFProperty assertNoMatchesProperty = owlModel.getRDFProperty(noMatchesName);
                if (assertNoMatchesProperty != null) {
                    test(aClass, assertNoMatchesProperty, false, "no matches", OWLIcons.ASSERT_FALSE, results);
                }
                return results;
            }
        }
        return Collections.EMPTY_LIST;
    }


    private void test(RDFSClass aClass,
                      RDFProperty assertProperty,
                      boolean expected,
                      String expectedString,
                      String iconName,
                      List results) {
        OWLModel owlModel = aClass.getOWLModel();
        Iterator it = aClass.listPropertyValues(assertProperty);
        while (it.hasNext()) {
            Object value = it.next();
            if (value instanceof String) {
                String str = (String) value;
                try {
                    QueryResults queryResults = owlModel.executeSPARQLQuery(str);
                    if (queryResults.hasNext() != expected) {
                        DefaultOWLTestResult r = new DefaultOWLTestResult("Query asserted to have " +
                                expectedString + ": " + str,
                                aClass, OWLTestResult.TYPE_ERROR, this, OWLIcons.getImageIcon(iconName));
                        r.setUserObject(str);
                        results.add(r);
                    }
                }
                catch (Exception ex) {
                    results.add(new DefaultOWLTestResult("Query could not be executed: " + str + ". " + ex.getMessage(), aClass, OWLTestResult.TYPE_WARNING, this));
                }
            }
        }
    }
}
