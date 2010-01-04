package edu.stanford.smi.protegex.owl.ui.explorer;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.resource.Colors;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExplorerRenderer extends FrameRenderer {

    private boolean expanded;

    private ExplorerTreeNode node;


    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        this.expanded = expanded;
        if (value instanceof ExplorerTreeNode) {
            node = (ExplorerTreeNode) value;
        }
        return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
    }


    protected void loadCls(Cls cls) {
        setMainIcon(getIcon(cls));
        String str = node.toString(expanded);
        if (cls instanceof OWLAnonymousClass) {
            setGrayedText(true);
        }
        setMainText(str);
        setBackgroundSelectionColor(Colors.getClsSelectionColor());
    }
}
