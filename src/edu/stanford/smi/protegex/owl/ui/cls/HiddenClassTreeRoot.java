package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.ui.LazyTreeNodeFrameComparator;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protege.util.LazyTreeRoot;
import edu.stanford.smi.protegex.owl.model.OWLModel;

import java.util.*;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         23-Feb-2006
 */
public class HiddenClassTreeRoot extends LazyTreeRoot {

    public HiddenClassTreeRoot(OWLModel owlModel) {
        super(getHiddenFrames(owlModel));
    }

    private static Collection getHiddenFrames(OWLModel owlModel) {
        Set<Frame> hiddenRoots = new HashSet();
        Iterator<Frame> i = owlModel.getProject().getHiddenFrames().iterator();
        while(i.hasNext()){
            Frame f = i.next();
            if (f instanceof Cls && f.isEditable()){
                hiddenRoots.add(f);
            }
        }
        return hiddenRoots;
    }

    public HiddenClassTreeRoot(Collection roots) {
        super(filter(roots));
    }

    public LazyTreeNode createNode(Object o) {
        return new HiddenClassTreeNode(this, (Cls) o);
    }

    protected static Collection filter(Collection roots) {
        Collection visibleRoots = new ArrayList(roots);
        Cls firstRoot = (Cls) CollectionUtilities.getFirstItem(roots);
        if (firstRoot != null && !firstRoot.getProject().getDisplayHiddenClasses()) {
            Iterator i = visibleRoots.iterator();
            while (i.hasNext()) {
                Cls cls = (Cls) i.next();
                if (cls.isVisible()) {
                    i.remove();
                }
            }
        }
        return visibleRoots;
    }

    public Comparator getComparator() {
        return new LazyTreeNodeFrameComparator();
    }
}
