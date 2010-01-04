package edu.stanford.smi.protegex.owl.ui.search.finder;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.results.HostResourceDisplay;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         19-Oct-2005
 */
public class FindResultsTableView extends AbstractFindResultsView {

    private JTable table;

    private ResultsViewModelFind find;

    protected FindResultsTableView(ResultsViewModelFind find, HostResourceDisplay hrd) {
        super(hrd);

        setLayout(new BorderLayout(6, 6));

        this.find = find;

        table = new JTable(find);

        TableColumn tc;
        TableCellRenderer ren = ResourceRenderer.createInstance();
        tc = table.getColumnModel().getColumn(FindResult.RESOURCE_NAME);
        tc.setCellRenderer(ren);
        tc = table.getColumnModel().getColumn(FindResult.PROPERTY_NAME);
        tc.setCellRenderer(ren);
        tc = table.getColumnModel().getColumn(FindResult.LANG);
        tc.setMinWidth(40);
        tc.setMaxWidth(40);

        JScrollPane scroller = new JScrollPane(table);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.getViewport().setBackground(table.getBackground());

        add(scroller, BorderLayout.CENTER);
    }

    public RDFResource getSelectedResource() {
        int row = table.getSelectedRow();
        return (RDFResource) find.getElementAt(row);
    }

    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        if (d.height < 400) {
            d.height = 400;
        }
        return d;
    }

    public void addMouseListener(MouseListener l) {
        table.addMouseListener(l);
    }

    public void addKeyListener(KeyListener l) {
        table.addKeyListener(l);
    }

    public void requestFocus() {
        table.requestFocus();
    }
}
