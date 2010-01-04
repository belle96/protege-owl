package edu.stanford.smi.protegex.owl.ui.components.annotations;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.View;
import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class AnnotationsValueRenderer implements TableCellRenderer {

    private JTextArea textArea;

    private JLabel label;

    private JLabel langLabel;

    public static final int EXTRA_SPACING = 4;

    private AnnotationsTableCellHolder resourceHolder;

    private AnnotationsTableCellHolder plainTextPropertyValHolder;

    private AnnotationsTableCellHolder langHolder;

    public AnnotationsValueRenderer() {
        super();
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFocusable(true);
        textArea.setOpaque(false);
        plainTextPropertyValHolder = new AnnotationsTableCellHolder(textArea, BorderLayout.CENTER);
        label = new JLabel();
        label.setOpaque(false);
	    if(System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
		    // Adjust border on label due to some silly windows 'feature'
		    label.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
	    }
	    resourceHolder = new AnnotationsTableCellHolder(label, BorderLayout.NORTH);
        langLabel = new JLabel();
        langHolder = new AnnotationsTableCellHolder(langLabel, BorderLayout.NORTH);
    }


    public Component getTableCellRendererComponent(JTable table,
                                                   Object o,
                                                   boolean selected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int col) {
        // Ensure that the row is the correct height.  We want to adjust the
	    // row height if it hasn't been adjusted by the cell editor, so check
	    // to ensure that the value of the property isn't being edited.
        if ((table.getEditingRow() == row && table.getEditingColumn() == AnnotationsTableModel.COL_VALUE) == false) {
            int rowHeight = getRowHeight(table, row);
            if (table.getRowHeight(row) != rowHeight) {
                table.setRowHeight(row, rowHeight);
            }
        }
        if (col == AnnotationsTableModel.COL_PROPERTY) {
            return getResourceComponent((RDFResource) o, selected, hasFocus);
        }
        else if (col == AnnotationsTableModel.COL_VALUE) {
            if (o instanceof RDFResource) {
                return getResourceComponent((RDFResource) o, selected, hasFocus);
            }
            else {
                textArea.setText(o != null ? o.toString() : "");
                plainTextPropertyValHolder.setColors(selected, hasFocus);
                return plainTextPropertyValHolder;
            }
        }
        else {
            langLabel.setText(o != null ? o.toString() : "");
            langHolder.setColors(selected, hasFocus);
            return langHolder;
        }
    }


    private JComponent getResourceComponent(RDFResource resource, boolean selected, boolean focused) {
        label.setText(resource.getBrowserText());
        label.setIcon(ProtegeUI.getIcon(resource));
        resourceHolder.setColors(selected, focused);
        return resourceHolder;
    }


    private int getRowHeight(JTable table,
                             int row) {
        Object val = table.getValueAt(row, AnnotationsTableModel.COL_VALUE);
        if (val instanceof String) {
            String text = val.toString();
            textArea.setText(text);
            View v = textArea.getUI().getRootView(textArea);
            v.setSize(table.getColumnModel().getColumn(AnnotationsTableModel.COL_VALUE).getWidth(), Integer.MAX_VALUE);
            int height = (int) v.getPreferredSpan(View.Y_AXIS) + 4;
            if (height < table.getRowHeight()) {
                height = table.getRowHeight();
            }
            return height;
        }
        return table.getRowHeight();
    }


}

