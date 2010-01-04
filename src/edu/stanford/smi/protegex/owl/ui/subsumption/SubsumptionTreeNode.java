package edu.stanford.smi.protegex.owl.ui.subsumption;

import edu.stanford.smi.protege.event.FrameAdapter;
import edu.stanford.smi.protege.event.FrameEvent;
import edu.stanford.smi.protege.event.FrameListener;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.ui.LazyTreeNodeFrameComparator;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

import java.util.*;

/**
 * A LazyTreeNode for an inferred or asserted subsumption relationship between classes.
 *
 * @author Holger Knublauch   <holger@knublauch.com>
 */
public class SubsumptionTreeNode extends LazyTreeNode {

    private FrameListener frameListener = new FrameAdapter() {

        public void browserTextChanged(FrameEvent event) {
            notifyNodeChanged();
        }


        public void ownSlotValueChanged(FrameEvent event) {
            notifyNodeChanged();
            if (event.getSlot().equals(ownSlot)) {
                reload();
            }
        }


        public void visibilityChanged(FrameEvent event) {
            notifyNodeChanged();
        }
    };

    private Slot ownSlot;


    public SubsumptionTreeNode(LazyTreeNode parentNode, Cls parentCls, Slot ownSlot) {
        super(parentNode, parentCls);
        this.ownSlot = ownSlot;
        parentCls.addFrameListener(frameListener);
    }


    protected LazyTreeNode createNode(Object o) {
        return new SubsumptionTreeNode(this, (Cls) o, ownSlot);
    }


    protected void dispose() {
        super.dispose();
        getCls().removeFrameListener(frameListener);
    }


    protected int getChildObjectCount() {
        return getChildObjects().size();
    }


    protected Collection getChildObjects() {
        Collection result = new ArrayList();
        Cls cls = getCls();
        List list = new ArrayList(cls.getDirectOwnSlotValues(ownSlot));
        Collections.sort(list);
        for (Iterator it = list.iterator(); it.hasNext();) {
            Cls c = (Cls) it.next();
            if (c.isVisible()) {
                result.add(c);
            }
        }
        // Remove all equivalent classes that have other superclasses as well
        if(cls instanceof OWLNamedClass && Model.Slot.DIRECT_SUBCLASSES.equals(ownSlot.getName())) {
            Iterator equis = ((OWLNamedClass)cls).getEquivalentClasses().iterator();
            while(equis.hasNext()) {
                RDFSClass equi = (RDFSClass) equis.next();
                if(equi instanceof OWLNamedClass && equi.getSuperclassCount() > 1) {
                    result.remove(equi);
                }
            }
        }
        return result;
    }


    public Cls getCls() {
        return (Cls) getUserObject();
    }


    protected Comparator getComparator() {
        return new LazyTreeNodeFrameComparator();
    }


    protected void notifyNodeChanged() {
        notifyNodeChanged(this);
    }


    public String toString() {
        return "SubsumptionTreeNode(" + getCls() + ")";
    }
}
