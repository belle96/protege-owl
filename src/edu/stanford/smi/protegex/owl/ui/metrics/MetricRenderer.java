package edu.stanford.smi.protegex.owl.ui.metrics;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Apr 21, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class MetricRenderer extends DefaultTreeCellRenderer {

    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {
        JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
        if (treeNode.getUserObject() instanceof MetricObject) {
            MetricObject obj = (MetricObject) treeNode.getUserObject();
            String text = "<html><body>" +
                    "<font color=\"#606060\">" +
                    obj.getMetricName() + "</font>: " +
                    obj.getValue() +
                    "</body></html>";
            label.setIcon(MetricsPanel.dataIcon);
            label.setText(text);
        }
        else {
            label.setIcon(MetricsPanel.headerIcon);
            String text = "<html><body>" +
                    "<font color=\"#000090\">" +
                    treeNode.getUserObject().toString() + "</font>" +
                    "</body></html>";
            label.setText(text);
        }

        return label;
    }
}

