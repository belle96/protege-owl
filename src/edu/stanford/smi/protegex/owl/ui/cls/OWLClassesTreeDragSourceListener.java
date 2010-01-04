package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.ui.ClsesTreeDragSourceListener;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

import javax.swing.tree.TreePath;
import java.util.Collection;
import java.util.Iterator;

/**
 * A special handler of drag and drop for OWL classes: The core Protege one would
 * remove named superclasses even if they were part of an equivalent intersection.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLClassesTreeDragSourceListener extends ClsesTreeDragSourceListener {

    public void doMove(Collection paths) {
        Iterator i = paths.iterator();
        while (i.hasNext()) {
            TreePath path = (TreePath) i.next();
            LazyTreeNode draggedNode = (LazyTreeNode) path.getLastPathComponent();
            LazyTreeNode draggedNodeParent = (LazyTreeNode) draggedNode.getParent();
            Cls draggedCls = (Cls) draggedNode.getUserObject();
            Cls draggedClsParent = (Cls) draggedNodeParent.getUserObject();
            if (draggedCls instanceof OWLNamedClass) {
                OWLNamedClass namedClass = (OWLNamedClass) draggedCls;
                if (namedClass.isDefinedClass()) {
                    for (Iterator it = namedClass.getEquivalentClasses().iterator(); it.hasNext();) {
                        RDFSClass equi = (RDFSClass) it.next();
                        if (equi instanceof OWLIntersectionClass) {
                            if (((OWLIntersectionClass) equi).hasOperandWithBrowserText(draggedClsParent.getBrowserText())) {
                                return;
                            }
                        }
                    }
                }
            }
            draggedCls.removeDirectSuperclass(draggedClsParent);
        }
    }
}
