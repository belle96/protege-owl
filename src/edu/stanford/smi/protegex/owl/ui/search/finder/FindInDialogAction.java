package edu.stanford.smi.protegex.owl.ui.search.finder;

import edu.stanford.smi.protege.ui.ProjectManager;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.ui.results.HostResourceDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         03-Oct-2005
 */
public class FindInDialogAction extends AbstractFindAction {

    private static Dimension savedSize = new Dimension(400, 200);

    AbstractFindResultsView view;

    FindResultsPanel resultsPanel;


    public FindInDialogAction(ResultsViewModelFind find, Icon icon, HostResourceDisplay hrd, boolean allowSave) {
        super(find, icon, hrd, allowSave);
    }


    protected void showResults(AbstractFindResultsView view) {

        this.view = view;

        resultsPanel = new FindResultsPanel(findModel.getFind(), view);
        findModel.getFind().addResultListener(new SearchAdapter() {
            public void searchEvent(Find source) {
                rename(source.getSummaryText());
            }
        });

        // retain the size as changed by the user
        resultsPanel.setPreferredSize(savedSize);
        resultsPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                savedSize.setSize(e.getComponent().getWidth(),
                                  e.getComponent().getHeight());
            }
        });

        resultsPanel.setSaveResultsEnabled(allowSave);

        Component win = ProtegeUI.getTopLevelContainer(ProjectManager.getProjectManager().getCurrentProject());

        ModalDialogFactory fac = ProtegeUI.getModalDialogFactory();
        int result = fac.showDialog(win, resultsPanel, findModel.getFind().getSummaryText(),
                                    ModalDialogFactory.MODE_OK_CANCEL,
                                    new ModalDialogFactory.CloseCallback() {
                                        public boolean canClose(int result) {
                                            boolean canClose = true;
                                            if (result == ModalDialogFactory.OPTION_OK) {
                                                canClose = (FindInDialogAction.this.view.getSelectedResource() != null);
                                            }
                                            return canClose;
                                        }
                                    });

        switch (result) {
            case ModalDialogFactory.OPTION_OK:
                resultsPanel.selectResource();
                break;
            case ModalDialogFactory.OPTION_CANCEL:
                synchronized (findModel) {
                    findModel.getFind().cancelSearch();
                    findModel.getFind().waitForSearchComplete();
                    findModel.getFind().reset();
                }
                break;
        }
    }

    private void rename(String name) {
        Object ancestor = resultsPanel.getTopLevelAncestor();
        if (ancestor != null && ancestor instanceof JDialog) {
            ((JDialog) ancestor).setTitle(name);
        }
    }
    
    
}
