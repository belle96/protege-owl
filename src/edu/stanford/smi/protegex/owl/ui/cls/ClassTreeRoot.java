package edu.stanford.smi.protegex.owl.ui.cls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.ui.ParentChildNodeComparator;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protege.util.LazyTreeRoot;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * Tree Root for the superclass-subclass relationship
 *
 * @author    Ray Fergerson <fergerson@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassTreeRoot extends LazyTreeRoot {

    public ClassTreeRoot(Cls root) {
        this(root, OWLUI.getSortClassTreeOption());
    }

    public ClassTreeRoot(Cls root, boolean isSorted) {
        super(root, isSorted);
    }
    
    public ClassTreeRoot(Collection roots) {
        this(filter(roots), OWLUI.getSortClassTreeOption());
    }
    
    public ClassTreeRoot(Collection roots, boolean isSorted) {
        super(filter(roots), isSorted);
    }

    public LazyTreeNode createNode(Object o) {
        return new ClassTreeNode(this, (Cls) o);
    }

    private static Collection filter(Collection roots) {
        Collection visibleRoots = new ArrayList(roots);
        Cls firstRoot = (Cls) CollectionUtilities.getFirstItem(roots);
        if (firstRoot != null && !firstRoot.getProject().getDisplayHiddenClasses()) {
            Iterator i = visibleRoots.iterator();
            while (i.hasNext()) {
                Cls cls = (Cls) i.next();
                if (!cls.isVisible()) {
                    i.remove();
                }
            }
        }
        return visibleRoots;
    }

    public Comparator getComparator() {
    	return new ParentChildNodeComparator();
    }
}
