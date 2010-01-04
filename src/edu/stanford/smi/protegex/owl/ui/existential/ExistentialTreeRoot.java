package edu.stanford.smi.protegex.owl.ui.existential;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.ui.LazyTreeNodeFrameComparator;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protege.util.LazyTreeRoot;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

import java.util.Comparator;

/**
 * A LazyTreeRoot for the root of a transitivity tree.
 *
 * @author Holger Knublauch   <holger@knublauch.com>
 */
public class ExistentialTreeRoot extends LazyTreeRoot {

    private OWLObjectProperty existentialProperty;

    private Slot superclassesSlot;


    public ExistentialTreeRoot(Cls root,
                               Slot superclassesSlot,
                               OWLObjectProperty existentialProperty) {
        super(root);
        this.superclassesSlot = superclassesSlot;
        this.existentialProperty = existentialProperty;
    }


    public LazyTreeNode createNode(Object o) {
        return new ExistentialTreeNode(this,
                (OWLNamedClass) o, superclassesSlot, existentialProperty);
    }


    public Comparator getComparator() {
        return new LazyTreeNodeFrameComparator();
    }
}
