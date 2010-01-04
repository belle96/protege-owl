package edu.stanford.smi.protegex.owl.ui.components.literaltable;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import edu.stanford.smi.protegex.owl.model.OWLDataRange;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.components.ComponentUtil;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * A JTable to view/edit an RDFSLiteralTableModel.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LiteralTable extends JTable {

    private JComboBox dataRangeComboBox;

    private JComboBox datatypeComboBox;

    private DefaultCellEditor defaultEditor;

    private JComboBox langComboBox;

    private LiteralTableModel tableModel;

    private JTextField textField;


    public LiteralTable(RDFProperty predicate) {
        tableModel = new LiteralTableModel(predicate);
        setModel(tableModel);
        setColumnWidth(LiteralTableModel.COL_VALUE, 200);
        setColumnWidth(LiteralTableModel.COL_TYPE, 100);

        textField = new JTextField();
        OWLUI.addCopyPastePopup(textField);
        defaultEditor = new DefaultCellEditor(textField);
        setDefaultEditor(Object.class, defaultEditor);

        dataRangeComboBox = new JComboBox();

        OWLModel owlModel = predicate.getOWLModel();
        datatypeComboBox = ComponentUtil.createDatatypeComboBox(owlModel);
        langComboBox = ComponentUtil.createLanguageComboBox(owlModel, null);

        TableColumn typeColumn = getColumnModel().getColumn(LiteralTableModel.COL_TYPE);
        typeColumn.setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof RDFSDatatype) {
                    value = ((RDFSDatatype) value).getLocalName();
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        TableColumn valueColumn = getColumnModel().getColumn(LiteralTableModel.COL_VALUE);
        valueColumn.setCellRenderer(new ResourceRenderer());

        updateCellEditor();
    }


    public void editCell(Object value) {
        int row = tableModel.getRow(value);
        editCell(row);
    }


    public void editCell(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            getSelectionModel().setSelectionInterval(row, row);
            scrollRectToVisible(getCellRect(row, LiteralTableModel.COL_VALUE, true));
            editCellAt(row, LiteralTableModel.COL_VALUE);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    textField.requestFocus();
                }
            });
        }
    }
    
    public TableCellEditor getCellEditor(int row, int column) {
        if (column == LiteralTableModel.COL_VALUE) {
            RDFResource resource = tableModel.getSubject();
            RDFResource range = resource.getAllValuesFromOnTypes(tableModel.getPredicate());
            if (range instanceof OWLDataRange) {
                OWLDataRange dataRange = (OWLDataRange) range;
                Object[] values = dataRange.getOneOfValues().toArray();
                dataRangeComboBox.setModel(new DefaultComboBoxModel(values));
                return new DefaultCellEditor(dataRangeComboBox);
            }
        }
        return super.getCellEditor(row, column);
    }


    public LiteralTableModel getTableModel() {
        return tableModel;
    }


    private void setColumnWidth(int column, int width) {
        TableColumn col = getColumnModel().getColumn(column);
        col.setPreferredWidth(width);
    }


    public void setSelectedRow(Object value) {
        int row = tableModel.getRow(value);
        if (row >= 0) {
            getSelectionModel().setSelectionInterval(row, row);
        }
    }


    public void setSubject(RDFResource subject) {
    	stopEditing();
    	
        tableModel.setSubject(subject);
        updateCellEditor();
    }


    public void stopEditing() {
        TableColumn typeColumn = getColumnModel().getColumn(LiteralTableModel.COL_TYPE);
        typeColumn.getCellEditor().stopCellEditing();
        defaultEditor.stopCellEditing();
    }


    private void updateCellEditor() {
        TableColumn typeColumn = getColumnModel().getColumn(LiteralTableModel.COL_TYPE);
        if (tableModel.isStringProperty()) {
            typeColumn.setCellEditor(new DefaultCellEditor(langComboBox));
        }
        else {
            typeColumn.setCellEditor(new DefaultCellEditor(datatypeComboBox));
        }
    }
    
}
