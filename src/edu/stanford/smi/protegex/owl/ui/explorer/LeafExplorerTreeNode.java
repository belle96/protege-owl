package edu.stanford.smi.protegex.owl.ui.explorer;

import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LeafExplorerTreeNode extends ExplorerTreeNode {

    public LeafExplorerTreeNode(LazyTreeNode parent, RDFSClass cls, ExplorerFilter filter) {
        super(parent, cls, filter);
    }


    protected List createChildObjects() {
        return Collections.EMPTY_LIST;
    }
}
