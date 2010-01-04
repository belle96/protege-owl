package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.util.ComponentFactory;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourcedisplay.ResourceDisplay;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SecondaryHierarchyPanel extends HierarchyPanel {

    private Action closeAction = new AbstractAction("Close hierarchy",
            OWLIcons.getImageIcon("CloseHierarchy")) {
        public void actionPerformed(ActionEvent e) {
            close();
        }
    };

    private Action startSynchronizeAction = new AbstractAction("Synchronize this hierarchy",
            OWLIcons.getImageIcon("StartSynchronizeHierarchy")) {
        public void actionPerformed(ActionEvent e) {
            startSynchronize();
        }
    };

    private Action stopSynchronizeAction = new AbstractAction("Stop synchronizing this hierarchy",
            OWLIcons.getImageIcon("StopSynchronizeHierarchy")) {
        public void actionPerformed(ActionEvent e) {
            stopSynchronize();
        }
    };


    public SecondaryHierarchyPanel(Hierarchy hierarchy, HierarchyManager hierarchyManager) {
        super(hierarchy, hierarchyManager, false, null);
        stopSynchronizeAction.setEnabled(false);
        ComponentFactory.addToolBarButton(toolBar, startSynchronizeAction, ResourceDisplay.SMALL_BUTTON_WIDTH);
        ComponentFactory.addToolBarButton(toolBar, stopSynchronizeAction, ResourceDisplay.SMALL_BUTTON_WIDTH);
        ComponentFactory.addToolBarButton(toolBar, closeAction, ResourceDisplay.SMALL_BUTTON_WIDTH);
    }


    private void close() {
        hierarchyManager.close(hierarchy);
    }


    public boolean isSynchronized() {
        return stopSynchronizeAction.isEnabled();
    }


    public void startSynchronize() {
        startSynchronizeAction.setEnabled(false);
        stopSynchronizeAction.setEnabled(true);
    }


    public void stopSynchronize() {
        startSynchronizeAction.setEnabled(true);
        stopSynchronizeAction.setEnabled(false);
    }
}
