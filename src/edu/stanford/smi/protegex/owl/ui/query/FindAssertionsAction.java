package edu.stanford.smi.protegex.owl.ui.query;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.Task;
import edu.stanford.smi.protegex.owl.testing.OWLTest;
import edu.stanford.smi.protegex.owl.testing.OWLTestLibrary;
import edu.stanford.smi.protegex.owl.testing.RDFResourceTest;
import edu.stanford.smi.protegex.owl.testing.constraints.FindSPARQLAssertsTest;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.testing.AbstractOWLModelTestAction;
import edu.stanford.smi.protegex.owl.ui.testing.OWLTestResultsPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FindAssertionsAction extends AbstractOWLModelTestAction {

    public FindAssertionsAction(OWLModel owlModel) {
        this.owlModel = owlModel;
        putValue(Action.SMALL_ICON, OWLIcons.getImageIcon(getIconFileName()));
        putValue(Action.NAME, "Find all SPARQL Assertions...");
    }


    protected OWLTestResultsPanel getTestResultsPanel(OWLModel owlModel, List results) {
        return new OWLTestResultsPanel(owlModel, results, null, false) {
            public String getTabName() {
                return "SPARQL Assertions";
            }


            public Icon getIcon() {
                return OWLIcons.getImageIcon(OWLIcons.FIND_ASSERTS);
            }
        };
    }


    public String getName() {
        return "Find all assertions...";
    }


    public String getIconFileName() {
        return OWLIcons.FIND_ASSERTS;
    }


    protected OWLTest[] getOWLTests() {
        return new OWLTest[]{
                OWLTestLibrary.getOWLTest(FindSPARQLAssertsTest.class)
        };
    }


    protected java.util.List run(OWLTest[] tests, Task task) {
        java.util.List results = new ArrayList();
        runOWLInstanceTest(results, (RDFResourceTest) tests[0]);
        return results;
    }

}
