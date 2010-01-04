package edu.stanford.smi.protegex.owl.ui.subsumption;

import edu.stanford.smi.protege.action.DeleteInstancesAction;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.AllowableAction;
import edu.stanford.smi.protege.util.CollectionUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * A SubsumptionTreePanel with additional support for editing the class hierarchy.
 * This adds create and delete buttons, and supports drag and drop.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class EditableSubsumptionTreePanel extends SubsumptionTreePanel {

    public EditableSubsumptionTreePanel(Cls root, Slot subclassesSlot,
                                        Slot superclassesSlot) {
        super(root, subclassesSlot, superclassesSlot, false);

        Action createAction = createCreateAction();
        if (createAction != null) {
            getLabeledComponent().addHeaderButton(createAction);
        }

        Action deleteAction = createDeleteAction();
        if (deleteAction != null) {
            getLabeledComponent().addHeaderButton(deleteAction);
        }
    }


    protected Action createCreateAction() {

        AllowableAction action = new AllowableAction("Create subclass", null,
                Icons.getCreateIcon(), this) {

            public void actionPerformed(ActionEvent e) {
                Collection parents = getSelection();
                if (!parents.isEmpty()) {
                    Cls cls = getOWLModel().createSubclass(null, parents);
                    extendSelection(cls);
                }
            }


            public void onSelectionChange() {
                super.onSelectionChange();
                setAllowed(!getSelection().isEmpty());
            }
        };
        action.setEnabled(false);
        return action;
    }


    protected Action createDeleteAction() {
        AllowableAction action = new DeleteInstancesAction(this) {
            public void onAboutToDelete() {
                removeSelection();
            }


            public void onSelectionChange() {
                boolean isEditable = false;
                Frame frame = (Frame) CollectionUtilities.getFirstItem(getSelection());
                if (frame != null) {
                    isEditable = frame.isEditable();
                }
                setAllowed(isEditable);
            }
        };
        action.setEnabled(false);
        return action;
    }
}
