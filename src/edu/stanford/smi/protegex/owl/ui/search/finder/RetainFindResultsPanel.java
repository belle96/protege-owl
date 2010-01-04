package edu.stanford.smi.protegex.owl.ui.search.finder;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.ProjectManager;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanel;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanelManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * A JPanel for modal dialogs that can be used to String-matching search for a certain
 * value of a selected Slot.
 *
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         03-Oct-2005
 */
public class RetainFindResultsPanel extends ResultsPanel {

    private FindResultsPanel resultsPanel;

    private String tabName = "Search";


    private Action refreshAction = new AbstractAction("Refresh", OWLIcons.getImageIcon("Refresh")) {
        public void actionPerformed(ActionEvent e) {
            resultsPanel.refresh();
        }
    };

    public RetainFindResultsPanel(Find find, AbstractFindResultsView view) {
        super(find.getModel());

        resultsPanel = new FindResultsPanel(find, view);
        find.addResultListener(new SearchAdapter() {
            public void searchEvent(Find source) {
                rename(source.getSummaryText());
            }
        });

        setCenterComponent(resultsPanel);

        addButton(refreshAction);
    }

    public RetainFindResultsPanel(OWLModel owlModel, FindResultsPanel panel) {
        super(owlModel);

        resultsPanel = panel;
        setCenterComponent(resultsPanel);
        addButton(refreshAction);
    }

    public Icon getIcon() {
        return Icons.getFindIcon();
    }

    public String getTabName() {
        return tabName;
    }

    private void rename(String label) {
        tabName = label;
        OWLModel omodel = (OWLModel) ProjectManager.getProjectManager().getCurrentProject().getKnowledgeBase();
        JTabbedPane t = ResultsPanelManager.getTabbedPane(omodel);
        if (t != null) {
            for (int i = 0; i < t.getTabCount(); i++) {
                ResultsPanel tab = (ResultsPanel) t.getComponentAt(i);
                if (tab == this) {
                    t.setMnemonicAt(i, KeyEvent.VK_F);
                    t.setTitleAt(i, tabName);
                }
            }
        }
        revalidate();
    }

    public void requestDispose(JComponent source) {
        //ignore
    }
}
