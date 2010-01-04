package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExpandTreeAction extends ResourceAction {

    private static final int MAX_EXPANSIONS = 1000;


    public ExpandTreeAction() {
        super("Expand", Icons.getBlankIcon(), TreePanel.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        TreePanel treePanel = (TreePanel) getComponent();
        ComponentUtilities.fullSelectionExpand(treePanel.getTree(), MAX_EXPANSIONS);
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof TreePanel;
    }
}
