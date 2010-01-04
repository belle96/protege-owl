package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.util.PopupMenuMouseListener;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FrameListPopupMenuMouseListener extends PopupMenuMouseListener {

    private JList list;


    public FrameListPopupMenuMouseListener(JList list) {
        super(list);
        this.list = list;
    }


    protected JPopupMenu getPopupMenu() {
        Object[] selection = list.getSelectedValues();
        if (selection.length == 1) {
            Instance instance = (Instance) selection[0];
            if (instance instanceof RDFResource) {
                JPopupMenu menu = new JPopupMenu();
                ResourceActionManager.addResourceActions(menu, list, (RDFResource) instance);
                if (menu.getSubElements().length > 0) {
                    return menu;
                }
            }
        }
        return null;
    }


    protected void setSelection(JComponent c, int x, int y) {
        final int listSize = list.getModel().getSize();
        Rectangle r = list.getCellBounds(0, listSize - 1);
        if (r.contains(x, y)) {
            int row = y / (r.height / listSize);
            if (row < listSize) {
                list.setSelectedIndex(row);
            }
        }
    }
}
