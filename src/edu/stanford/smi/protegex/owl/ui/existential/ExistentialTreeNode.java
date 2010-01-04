package edu.stanford.smi.protegex.owl.ui.existential;

import edu.stanford.smi.protege.event.FrameAdapter;
import edu.stanford.smi.protege.event.FrameEvent;
import edu.stanford.smi.protege.event.FrameListener;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.ui.LazyTreeNodeFrameComparator;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLClass;

import java.util.Collection;
import java.util.Comparator;

/**
 * A LazyTreeNode for a relationship between classes via someValuesFrom
 * restrictions on a (transitive) property.
 *
 * @author Holger Knublauch   <holger@knublauch.com>
 */
public class ExistentialTreeNode extends LazyTreeNode {

    private ExistentialFillerProvider fillerProvider;

    private FrameListener frameListener = new FrameAdapter() {

        public void browserTextChanged(FrameEvent event) {
            notifyNodeChanged();
        }


        public void ownSlotValueChanged(FrameEvent event) {
            notifyNodeChanged();
            if (event.getSlot().equals(superclassesSlot)) {
                reload();
            }
        }


        public void visibilityChanged(FrameEvent event) {
            notifyNodeChanged();
        }
    };

    private Slot superclassesSlot;

    private OWLObjectProperty existentialProperty;


    public ExistentialTreeNode(LazyTreeNode parentNode,
                               OWLClass parentCls,
                               Slot superclassesSlot,
                               OWLObjectProperty existentialProperty) {
        super(parentNode, parentCls);
        this.fillerProvider = new ExistentialFillerProvider(existentialProperty);
        parentCls.accept(fillerProvider);
        this.superclassesSlot = superclassesSlot;
        this.existentialProperty = existentialProperty;
        ((Cls) parentCls).addFrameListener(frameListener);
    }


    protected LazyTreeNode createNode(Object o) {
        return new ExistentialTreeNode(this, (OWLClass) o, superclassesSlot, existentialProperty);
    }


    protected void dispose() {
        super.dispose();
        ((Cls) getOWLClass()).removeFrameListener(frameListener);
    }


    protected int getChildObjectCount() {
        return fillerProvider.getFillers().size();
    }


    protected Collection getChildObjects() {
        return fillerProvider.getFillers();
    }

    public OWLClass getOWLClass() {
        return (OWLClass) getUserObject();
    }

    public void reload() {
        fillerProvider.reset();
        getOWLClass().accept(fillerProvider);
        super.reload();
    }

    protected Comparator getComparator() {
        return new LazyTreeNodeFrameComparator();
    }


    protected void notifyNodeChanged() {
        notifyNodeChanged(this);
    }


    public String toString() {
        return "ExistentialTreeNode(" + getOWLClass() + ")";
    }
}
