package edu.stanford.smi.protegex.owl.ui.actions;

import javax.swing.*;

/**
 * An interface for containers of a JTree.
 * This is needed to generalize expand and collapse actions.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface TreePanel {

    final static String GROUP = "Tree";


    JTree getTree();
}
