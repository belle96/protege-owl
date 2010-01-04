package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CollapseTreeAction extends ResourceAction {

    public CollapseTreeAction() {
        super("Collapse", Icons.getBlankIcon(), TreePanel.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        TreePanel treePanel = (TreePanel) getComponent();
        ComponentUtilities.fullSelectionCollapse(treePanel.getTree());
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof TreePanel;
    }
}
