package edu.stanford.smi.protegex.owl.ui.subsumption;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.ui.LazyTreeNodeFrameComparator;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protege.util.LazyTreeRoot;

import java.util.Comparator;

/**
 * A LazyTreeRoot for a computed or asserted subsumption relationship between classes.
 *
 * @author Holger Knublauch   <holger@knublauch.com>
 * @author Ray Fergerson   <fergerson@smi.stanford.edu>
 */
public class SubsumptionTreeRoot extends LazyTreeRoot {

    private Slot ownSlot;


    public SubsumptionTreeRoot(Cls root, Slot ownSlot) {
        super(root);
        this.ownSlot = ownSlot;
    }


    public LazyTreeNode createNode(Object o) {
        return new SubsumptionTreeNode(this, (Cls) o, ownSlot);
    }


    public Comparator getComparator() {
        return new LazyTreeNodeFrameComparator();
    }
}