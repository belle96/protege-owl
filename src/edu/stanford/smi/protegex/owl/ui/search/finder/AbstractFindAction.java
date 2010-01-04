package edu.stanford.smi.protegex.owl.ui.search.finder;

import edu.stanford.smi.protegex.owl.ui.results.HostResourceDisplay;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         03-Oct-2005
 */
abstract class AbstractFindAction extends AbstractAction implements FindAction {

    private JTextComponent searchBox;

    protected ResultsViewModelFind findModel;

    protected boolean allowSave;

    private HostResourceDisplay hostResourceDisplay;

    public AbstractFindAction(ResultsViewModelFind find, Icon icon) {
        this(find, icon, null);
    }

    public AbstractFindAction(ResultsViewModelFind find, Icon icon, HostResourceDisplay hrd) {
        this(find, icon, hrd, false);
    }

    public AbstractFindAction(ResultsViewModelFind find, Icon icon, HostResourceDisplay hrd, boolean allowSave) {
        super(find.getFind().getDescription(), icon);
        this.findModel = find;
        this.hostResourceDisplay = hrd;
        this.allowSave = allowSave;
    }

    public void setTextBox(JTextComponent textBox) {
        this.searchBox = textBox;
    }

    public void actionPerformed(ActionEvent e) {

        String txt = "";

        if (searchBox != null) {
            txt = searchBox.getText();
        }

        findModel.getFind().startSearch(txt);
//        Map results = find.getResults();

        // for a single result, just go and select the resource
//        if ((results != null) && (results.size() == 1)) {
//            RDFResource result = (RDFResource) results.keySet().iterator().next();
//            OWLUI.selectResource(result, hrd);
//        }
//        else {
        // determine whether to show a table or a simple list
        if (findModel.getFind().getNumSearchProperties() > 1) {
            showResults(new FindResultsTableView(findModel, hostResourceDisplay));
        }
        else {
            showResults(new FindResultsListView(findModel, hostResourceDisplay));
        }
//        }

    }

    protected abstract void showResults(AbstractFindResultsView view);
}
