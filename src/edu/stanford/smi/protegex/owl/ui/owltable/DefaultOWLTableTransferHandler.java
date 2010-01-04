package edu.stanford.smi.protegex.owl.ui.owltable;

import edu.stanford.smi.protegex.owl.model.OWLModel;

import javax.swing.*;

/**
 * A default OWLTableTransferHandler that assumes that the class maintain their
 * rows after changing something.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLTableTransferHandler extends OWLTableTransferHandler {

    private int addCount = 0;  // Number of items added.

    private int addIndex = -1; // Location where items were added


    public DefaultOWLTableTransferHandler(OWLModel owlModel) {
        super(owlModel);
    }


    protected void cleanup(JComponent c, boolean remove) {
        JTable source = (JTable) c;
        if (remove && rows != null) {
            OWLTableModel model = (OWLTableModel) source.getModel();

            // If we are moving items around in the same table, we
            // need to adjust the rows accordingly, since those
            // after the insertion point have moved.
            if (addCount > 0) {
                for (int i = 0; i < rows.length; i++) {
                    if (rows[i] > addIndex) {
                        rows[i] += addCount;
                    }
                }
            }
            for (int i = rows.length - 1; i >= 0; i--) {
                model.deleteRow(rows[i]);
            }
        }
        rows = null;
        addCount = 0;
        addIndex = -1;
    }


    protected int importOWLClses(JComponent c, String clsesText) {
        addIndex = super.importOWLClses(c, clsesText);
        String[] values = clsesText.split("\n");
        addCount = values.length;
        return addIndex;
    }
}
