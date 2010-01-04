package edu.stanford.smi.protegex.owl.ui.components.annotations;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.components.ComponentUtil;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 14, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class AnnotationsLangEditor extends AbstractCellEditor implements TableCellEditor {

    private AnnotationsTableCellHolder langHolder;

    private JComboBox comboBox;


    public AnnotationsLangEditor(OWLModel model, JTable table) {
        comboBox = ComponentUtil.createLangCellEditor(model, table);
        JPanel holderPanel = new JPanel(new BorderLayout());
        holderPanel.add(comboBox, BorderLayout.NORTH);
        holderPanel.setOpaque(false);
        holderPanel.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        langHolder = new AnnotationsTableCellHolder(holderPanel, BorderLayout.CENTER);
        langHolder.setOpaque(false);
	    comboBox.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			    stopCellEditing();
		    }
	    });
    }


    public Object getCellEditorValue() {
        if (comboBox.getSelectedItem() != null) {
            return comboBox.getSelectedItem().toString();
        }
        else {
            return null;
        }
    }


    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {
        comboBox.setSelectedItem(value);
        return langHolder;
    }
}

