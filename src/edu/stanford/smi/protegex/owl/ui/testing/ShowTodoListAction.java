package edu.stanford.smi.protegex.owl.ui.testing;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.Task;
import edu.stanford.smi.protegex.owl.testing.OWLTest;
import edu.stanford.smi.protegex.owl.testing.OWLTestLibrary;
import edu.stanford.smi.protegex.owl.testing.RDFResourceTest;
import edu.stanford.smi.protegex.owl.testing.todo.TodoAnnotationOWLTest;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ShowTodoListAction extends AbstractOWLModelTestAction {

    public String getIconFileName() {
        return OWLIcons.TODO;
    }


    public String getName() {
        return "Show TODO list...";
    }


    protected OWLTest[] getOWLTests() {
        return new OWLTest[]{
                OWLTestLibrary.getOWLTest(TodoAnnotationOWLTest.class)
        };
    }


    protected OWLTestResultsPanel getTestResultsPanel(OWLModel owlModel, List results) {
        return new OWLTestResultsPanel(owlModel, results, null, false) {
            public String getTabName() {
                return "TODO List";
            }


            public Icon getIcon() {
                return OWLIcons.getTODOIcon();
            }
        };
    }


    protected List run(OWLTest[] tests, Task task) {
        List results = new ArrayList();
        runOWLInstanceTest(results, (RDFResourceTest) tests[0]);
        return results;
    }


    protected void showAllTestsPassedMessage(int count) {
        // JOptionPane.showMessageDialog(null, "No to-do list items found.");
    }
}
