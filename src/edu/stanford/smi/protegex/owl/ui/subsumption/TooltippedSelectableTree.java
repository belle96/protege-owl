package edu.stanford.smi.protegex.owl.ui.subsumption;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreePath;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.util.FrameWithBrowserText;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protege.util.LazyTreeRoot;
import edu.stanford.smi.protege.util.SelectableTree;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.owltable.OWLTable;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TooltippedSelectableTree extends SelectableTree {

    private static final long serialVersionUID = 6697428920472428637L;


    public TooltippedSelectableTree(Action doubleClickAction, LazyTreeRoot root) {
        super(doubleClickAction, root);
        final int oldDelay = ToolTipManager.sharedInstance().getDismissDelay();
        addMouseListener(new MouseAdapter() {
            public void mouseExited(MouseEvent e) {
                ToolTipManager.sharedInstance().setDismissDelay(oldDelay);
            }
        });
        setToolTipText(""); // Dummy to initialize the mechanism
    }


    public String getToolTipText(MouseEvent event) {
        int row = getRowForLocation(event.getX(), event.getY());
        if (row >= 0) {
            TreePath path = getPathForRow(row);
            if (path != null) {
                Object last = path.getLastPathComponent();
                RDFResource res = null;
                if (last instanceof LazyTreeNode) {
                    LazyTreeNode node = (LazyTreeNode) path.getLastPathComponent();
                    Object userObject = node.getUserObject();
                    if (userObject instanceof RDFResource) {
                    	res = (RDFResource) userObject;
                    } else if (userObject instanceof FrameWithBrowserText) {
                        Frame frame = ((FrameWithBrowserText)userObject).getFrame();
                        if (frame instanceof RDFResource) {
                            res = (RDFResource) frame;
                        }                        
                    }                    
                }
                if (res != null) {
                    ToolTipManager.sharedInstance().setDismissDelay(OWLTable.INFINITE_TIME);
                    return OWLUI.getOWLToolTipText(res);
                }
            }
        }
        return null;
    }
}
