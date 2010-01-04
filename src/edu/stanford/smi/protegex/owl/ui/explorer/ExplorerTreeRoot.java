package edu.stanford.smi.protegex.owl.ui.explorer;

import edu.stanford.smi.protege.ui.LazyTreeNodeFrameComparator;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protege.util.LazyTreeRoot;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

import java.util.Comparator;

/**
 * A LazyTreeRoot for the explorer tree.
 *
 * @author Holger Knublauch   <holger@knublauch.com>
 */
public class ExplorerTreeRoot extends LazyTreeRoot {

    private ExplorerFilter filter;


    public ExplorerTreeRoot(RDFSClass root, ExplorerFilter filter) {
        super(root);
        this.filter = filter;
    }


    public LazyTreeNode createNode(Object o) {
        return new RDFSNamedClassTreeNode(this, (RDFSNamedClass) o, filter);
    }


    public Comparator getComparator() {
        return new LazyTreeNodeFrameComparator();
    }
}
