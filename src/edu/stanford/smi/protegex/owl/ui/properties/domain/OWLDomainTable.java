package edu.stanford.smi.protegex.owl.ui.properties.domain;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protege.util.PopupMenuMouseListener;
import edu.stanford.smi.protege.util.SelectableTable;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceActionManager;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Collections;

/**
 * A JTable to display an OWLDomainModel.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLDomainTable extends SelectableTable {

    public final static int INFINITE_TIME = 1000000;

    public OWLDomainTable(final OWLDomainTableModel tableModel) {
        super();
        setModel(tableModel);
        setRowHeight(getFontMetrics(getFont()).getHeight());
        getTableHeader().setReorderingAllowed(false);
        setShowGrid(false);
        setRowMargin(0);
        setIntercellSpacing(new Dimension(0, 0));
        TableColumn owlColumn = getColumnModel().getColumn(0);
        FrameRenderer renderer = new ResourceRenderer() {
            protected Icon getClsIcon(Cls cls) {
                if (tableModel.isInherited(cls)) {
                    ImageIcon icon = ((RDFSNamedClass) cls).getImageIcon();
                    return OWLIcons.getInheritedIcon(icon, OWLIcons.CLASS_FRAME);
                }
                else {
                    return super.getClsIcon(cls);
                }
            }
        };
        owlColumn.setCellRenderer(renderer);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleDoubleClick();
                }
            }
        });
        addMouseListener(new PopupMenuMouseListener(this) {

            protected JPopupMenu getPopupMenu() {
                int[] rows = getSelectedRows();
                if (rows.length == 1) {
                    RDFSClass aClass = tableModel.getCls(rows[0]);
                    JPopupMenu menu = new JPopupMenu();
                    ResourceActionManager.addResourceActions(menu, OWLDomainTable.this, aClass);
                    return menu;
                }
                return null;
            }


            protected void setSelection(JComponent c, int x, int y) {
                int row = y / getRowHeight();
                if (row >= 0 && row < getRowCount()) {
                    getSelectionModel().setSelectionInterval(row, row);
                }
            }
        });
    }


    public String getToolTipText(MouseEvent event) {
        ToolTipManager.sharedInstance().setDismissDelay(INFINITE_TIME);
        int rowCount = getModel().getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Rectangle r = getCellRect(i, 0, false);
            r.setSize(getWidth(), r.height);
            if (r.contains(event.getPoint())) {
                RDFResource res = (RDFResource) getModel().getValueAt(i, 0);
                return OWLUI.getOWLToolTipText(res);
            }
        }
        return null;
    }

    /**
     * Overloaded to prevent the creation of the table header.
     * Found at <a href="http://www.codeguru.com/java/articles/180.shtml">CodeGuru</A>.
     */
    protected void configureEnclosingScrollPane() {
        Container p = getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                JViewport viewport = scrollPane.getViewport();
                if (viewport == null || viewport.getView() != this) {
                    return;
                }
                // scrollPane.setColumnHeaderView(getTableHeader());
                scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
                scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
            }
        }
    }


    public Collection getSelection() {
        OWLDomainTableModel tableModel = (OWLDomainTableModel) getModel();
        RDFProperty property = tableModel.getSlot();
        if (property != null) {
            return super.getSelection();
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }


    private void handleDoubleClick() {
        OWLDomainTableModel tableModel = (OWLDomainTableModel) getModel();
        int row = getSelectedRow();
        if (row >= 0 && row < tableModel.getRowCount()) {
            Cls cls = (Cls) tableModel.getValueAt(row, 0);
            if (cls != null) {
                tableModel.getSlot().getProject().show(cls);
            }
        }
    }
}
