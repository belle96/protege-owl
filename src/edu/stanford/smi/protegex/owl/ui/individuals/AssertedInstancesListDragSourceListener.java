package edu.stanford.smi.protegex.owl.ui.individuals;

import edu.stanford.smi.protege.util.ListDragSourceListener;

import javax.swing.*;
import java.util.Collection;

/**
 * Source side handling of the drag and drop operations on the instances tab.  Note that the source is always the
 * instances list and the target is always the classes pane.
 *
 * @author Ray Fergerson <fergerson@smi.stanford.edu>
 */
class AssertedInstancesListDragSourceListener extends ListDragSourceListener {

    public void doCopy(JComponent c, int[] indices, Collection draggedObjects) {
    }


    public void doMove(final JComponent c, int[] indices, Collection draggedObjects) {
    }
}
