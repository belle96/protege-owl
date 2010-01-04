package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.actions.TreePanel;

/**
 * An interface for panels that display a class tree.
 * This is needed by some actions (create class etc) to have a valid host container.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ClassTreePanel extends TreePanel {

    /**
     * Selects a given class in the tree.
     *
     * @param cls the class to select (and navigate to)
     */
    void setSelectedClass(RDFSClass cls);
}
