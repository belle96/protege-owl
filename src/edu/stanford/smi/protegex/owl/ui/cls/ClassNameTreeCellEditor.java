package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.ui.ParentChildNode;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.resourcedisplay.InstanceNameEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * A tree cell editor that allows the name of a class to be edited
 *
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         17-Jan-2006
 */
public class ClassNameTreeCellEditor extends DefaultCellEditor{

    RDFSClass cls;

    InstanceNameEditor component;

    JTree tree;

    JPanel panel;
    JLabel iconLabel;

    public ClassNameTreeCellEditor() {
        super(new InstanceNameEditor());
    }

    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {

        this.tree = tree;
        this.cls = (RDFSClass)((ParentChildNode)value).getUserObject();
        this.component = (InstanceNameEditor)super.getTreeCellEditorComponent(tree, cls.getName(), isSelected, expanded, leaf, row);
        this.component.setInstance(cls);
        this.component.addFocusListener(new FocusAdapter(){
            public void focusLost(FocusEvent e) {
                fireEditingCanceled();
            }
        });

        panel = new JPanel(new BorderLayout(1, 1));
        panel.setBackground(tree.getBackground());

        iconLabel = new JLabel();
        iconLabel.setBackground(panel.getBackground());
        iconLabel.setIcon(cls.getImageIcon());

        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(component, BorderLayout.EAST);

        return panel;
    }

    public Object getCellEditorValue() {
        return cls.getName();
    }

    protected void fireEditingStopped() {
        if (component.isValidName(component.getText())){
            // pretend cancelled to stop the tree crashing
            // as the default expects MutableTreeNodes
            super.fireEditingCanceled();
        }
    }

    public void cancelCellEditing() {
        super.cancelCellEditing();
        tree.setEditable(false);
    }
}

