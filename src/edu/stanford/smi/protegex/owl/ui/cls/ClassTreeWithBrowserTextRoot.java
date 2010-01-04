package edu.stanford.smi.protegex.owl.ui.cls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.FrameWithBrowserText;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protege.util.LazyTreeRoot;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;

public class ClassTreeWithBrowserTextRoot extends LazyTreeRoot {

    public ClassTreeWithBrowserTextRoot(Collection<? extends Cls> roots, boolean isSorted) {
        super(FrameWithBrowserText.getFramesWithBrowserText(filter(roots)), isSorted);
    }
    
    public ClassTreeWithBrowserTextRoot(Cls rootCls, boolean isSorted) {
        super(new FrameWithBrowserText(rootCls, rootCls.getBrowserText(), rootCls.getDirectTypes(), 
        		ProtegeUI.getPotentialIconName(rootCls)), isSorted);
    }

    @Override
    public LazyTreeNode createNode(Object o) {
        return new ClassTreeWithBrowserTextNode(this, (FrameWithBrowserText) o);
    }

    @Override
    protected Comparator getComparator() {
        // TODO Auto-generated method stub
        return null;
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

}
