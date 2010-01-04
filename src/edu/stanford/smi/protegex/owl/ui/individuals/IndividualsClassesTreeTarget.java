package edu.stanford.smi.protegex.owl.ui.individuals;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.util.*;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.Collection;

/**
 * The target side handler for drag and drop operations on the InstancesTab.  The drop operation can only occur on
 * a class (to change the direct type of the dragged instance).
 *
 * @author Ray Fergerson <fergerson@smi.stanford.edu>
 */
class IndividualsClassesTreeTarget extends TreeTarget {

    public IndividualsClassesTreeTarget() {
        super(false);
    }


    private boolean confirm(JComponent c, Collection instances) {
        boolean result = true;
        if (SystemUtilities.modalDialogInDropWorks()) {
            String text = "Do you want to change the class of ";
            if (instances.size() == 1) {
                text += "this instance";
            }
            else {
                text += "these instances";
            }
            int rval = ModalDialog.showMessageDialog(c, text, ModalDialog.MODE_OK_CANCEL);
            result = rval == ModalDialog.OPTION_OK;
        }
        return result;
    }


    public boolean doDrop(JTree tree, Object source, int row, Object area) {
        boolean succeeded = false;

        TreePath path = tree.getPathForRow(row);
        Cls targetCls = (Cls) ((LazyTreeNode) path.getLastPathComponent()).getUserObject();
        Instance sourceInstance = null;
        if (source instanceof Instance) {
            sourceInstance = (Instance) source;
        }
        else if (source instanceof FrameWithBrowserText) {
            sourceInstance = (Instance) ((FrameWithBrowserText) source).getFrame();
        }

        if (targetCls.isAbstract()) {
            // do nothing
        }
        else if (sourceInstance.hasDirectType(targetCls)) {
            Log.getLogger().warning("do nothing on drop");
        }
        else {
            if (sourceInstance instanceof Cls) {
                if (targetCls.isClsMetaCls()) {
                    sourceInstance.setDirectType(targetCls);
                    succeeded = true;
                }
            }
            else if (!targetCls.isClsMetaCls()) {
                sourceInstance.setDirectType(targetCls);
                succeeded = true;
            }
        }
        return succeeded;
    }


    public boolean doDrop(JTree tree, Collection sources, int row, Object area) {
        boolean succeeded = false;
        if (confirm(tree, sources)) {
            succeeded = super.doDrop(tree, sources, row, area);
        }
        return succeeded;
    }
}
