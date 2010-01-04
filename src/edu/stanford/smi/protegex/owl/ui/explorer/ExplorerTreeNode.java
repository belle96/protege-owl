package edu.stanford.smi.protegex.owl.ui.explorer;

import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.event.ClassAdapter;
import edu.stanford.smi.protegex.owl.model.event.ClassListener;

import java.util.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class ExplorerTreeNode extends LazyTreeNode {

    protected ExplorerFilter filter;

    private ClassListener listener = new ClassAdapter() {
        public void superclassAdded(RDFSClass cls, RDFSClass superclass) {
            updateChildren();
        }


        public void superclassRemoved(RDFSClass cls, RDFSClass superclass) {
            updateChildren();
        }
    };


    public ExplorerTreeNode(LazyTreeNode parent, RDFSClass cls, ExplorerFilter filter) {
        super(parent, cls);
        cls.addClassListener(listener);
        this.filter = filter;
    }


    protected abstract List createChildObjects();


    protected LazyTreeNode createNode(Object o) {
        return ExplorerTreeNodeFactory.create(this, (RDFSClass) o, filter);
    }


    private void deleteChildren() {
        super.dispose();
    }


    public void dispose() {
        getRDFSClass().removeClassListener(listener);
        deleteChildren();
    }


    protected Collection getChildObjects() {
        List results = new ArrayList();
        Iterator it = createChildObjects().iterator();
        while (it.hasNext()) {
            RDFSClass childClass = (RDFSClass) it.next();
            if (filter.isValidChild(getRDFSClass(), childClass)) {
                results.add(childClass);
            }
        }
        return results;
    }


    protected int getChildObjectCount() {
        return getChildObjects().size();
    }


    public ExplorerTreeNode getChildNode(int index) {
        return (ExplorerTreeNode) getChildAt(index);
    }


    protected Comparator getComparator() {
        return null;
    }


    public RDFSClass getRDFSClass() {
        return (RDFSClass) getUserObject();
    }


    public String toString(boolean expanded) {
        return getRDFSClass().getBrowserText();
    }


    private void updateChildren() {
        reload();
    }
}
