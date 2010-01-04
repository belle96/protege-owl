package edu.stanford.smi.protegex.owl.ui.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ToolTipManager;

import edu.stanford.smi.protege.util.SelectableList;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.owltable.OWLTable;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         28-Mar-2006
 */
public class TooltippedSelectableList extends SelectableList {

    public TooltippedSelectableList() {
        super();
        final int oldDelay = ToolTipManager.sharedInstance().getDismissDelay();
        addMouseListener(new MouseAdapter() {
            @Override
			public void mouseExited(MouseEvent e) {
                ToolTipManager.sharedInstance().setDismissDelay(oldDelay);
            }
        });
        setToolTipText(""); // Dummy to initialize the mechanism
    }


    @Override
	public String getToolTipText(MouseEvent event) {
        int row = locationToIndex(event.getPoint());
        if (row < 0) { return "";}
        Object o = getModel().getElementAt(row);
        if (o != null && o instanceof RDFResource) {
            ToolTipManager.sharedInstance().setDismissDelay(OWLTable.INFINITE_TIME);
            return OWLUI.getOWLToolTipText((RDFResource) o);
        }
        return null;
    }
}