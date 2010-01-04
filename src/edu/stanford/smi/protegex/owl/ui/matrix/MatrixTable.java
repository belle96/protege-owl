package edu.stanford.smi.protegex.owl.ui.matrix;

import edu.stanford.smi.protege.util.PopupMenuMouseListener;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceActionManager;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanel;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanelManager;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class MatrixTable extends JTable {

    private MatrixTableModel tableModel;


    public MatrixTable(MatrixTableModel aTableModel) {
        this.tableModel = aTableModel;
        tableModel.setTable(this);
        setModel(aTableModel);
        getTableHeader().setReorderingAllowed(false);
        setShowGrid(false);
        setRowMargin(0);
        setIntercellSpacing(new Dimension(0, 0));
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(Math.max(getFontMetrics(getFont()).getHeight(), 16));

        initColumns();

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleDoubleClick();
                }
            }
        });

        addMouseListener(new PopupMenuMouseListener(this) {
            protected JPopupMenu getPopupMenu() {
                JPopupMenu popupMenu = new JPopupMenu();
                int sel = getSelectedRow();
                if (sel >= 0 && sel < tableModel.getRowCount()) {
                    ResourceActionManager.addResourceActions(popupMenu, MatrixTable.this, tableModel.getInstance(sel));
                }
                if (popupMenu.getComponentCount() > 0) {
                    return popupMenu;
                }
                else {
                    return null;
                }
            }


            protected void setSelection(JComponent c, int x, int y) {
                int row = y / getRowHeight();
                if (row >= 0 && row < MatrixTable.this.tableModel.getRowCount()) {
                    getSelectionModel().setSelectionInterval(row, row);
                }
            }
        });

        getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTableHeader h = (JTableHeader) e.getSource();
                TableColumnModel columnModel = h.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = columnModel.getColumn(viewColumn).getModelIndex();
                if (tableModel.isSortableColumn(column)) {
                    setSortColumn(column);
                }
            }
        });
    }


    public void addColumn(MatrixColumn col) {
        int index = tableModel.getNewColumnIndex(col);
        tableModel.addColumn(col, index);
        addColumn(new TableColumn(index));
        initColumns();
        getTableHeader().repaint();
    }


    /**
     * Programmatically closes the container of this table, which
     * is usually a ResultsPanel.
     */
    public void close() {
        Container c = getParent();
        while (c != null && !(c instanceof ResultsPanel)) {
            c = c.getParent();
        }
        if (c != null) {
            ResultsPanel panel = (ResultsPanel) c;
            panel.close();
        }
    }


    public TableCellEditor getCellEditor(int row, int column) {
        MatrixColumn col = tableModel.getMatrixColumn(column);
        if (col instanceof EditableMatrixColumn) {
            return ((EditableMatrixColumn) col).getTableCellEditor();
        }
        return super.getCellEditor();
    }


    public void initColumns() {
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            MatrixColumn col = tableModel.getMatrixColumn(i);
            TableColumn column = getColumnModel().getColumn(i);
            column.setHeaderValue(col.getName());
            column.setCellRenderer(col.getCellRenderer());
            column.setPreferredWidth(col.getWidth());
        }
    }


    private void handleDoubleClick() {
        int sel = getSelectedRow();
        if (sel >= 0 && sel < tableModel.getRowCount()) {
            RDFResource instance = tableModel.getInstance(sel);
            ResultsPanelManager.showHostResource(instance);
        }
    }


    private void setSortColumn(int column) {
        tableModel.setSortColumn(column);
        initColumns();
    }


    public RDFResource getSelectedInstance() {
        int sel = getSelectedRow();
        if (sel >= 0) {
            return tableModel.getInstance(sel);
        }
        else {
            return null;
        }
    }
}
