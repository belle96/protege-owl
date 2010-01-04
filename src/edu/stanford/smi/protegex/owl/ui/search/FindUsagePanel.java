package edu.stanford.smi.protegex.owl.ui.search;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.event.ModelAdapter;
import edu.stanford.smi.protegex.owl.model.event.ModelListener;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

/**
 * A JPanel to display the results of a "Find usage" search.
 * This mainly consists of a JTable with a FindUsageTableModel and a view button.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FindUsagePanel extends ResultsPanel
        implements FindUsageTableModelColumns {

    private ModelListener listener = new ModelAdapter() {
        public void classDeleted(RDFSClass cls) {
            if (searchResource.equals(cls)) {
                close();
            }
        }


        public void individualDeleted(RDFResource resource) {
            if (searchResource.equals(resource)) {
                close();
            }
        }


        public void propertyDeleted(RDFProperty property) {
            if (searchResource.equals(property)) {
                close();
            }
        }
    };

    private OWLModel owlModel;

    private RDFResource searchResource;

    private FindUsageTableModel tableModel;

    private JTable table;

    private Action refreshAction = new AbstractAction("Refresh", OWLIcons.getImageIcon("Refresh")) {
        public void actionPerformed(ActionEvent e) {
            refresh();
        }
    };

    private Action viewAction = new AbstractAction("View resource", OWLIcons.getViewIcon()) {
        public void actionPerformed(ActionEvent e) {
            viewSelectedHostCls();
        }
    };


    public FindUsagePanel(final RDFResource searchResource, Collection items) {

        super(searchResource.getOWLModel());

        this.searchResource = searchResource;
        owlModel = searchResource.getOWLModel();
        owlModel.addModelListener(listener);
        tableModel = new FindUsageTableModel(owlModel, items);
        table = new JTable(tableModel);
        table.setRowHeight(getFontMetrics(getFont()).getHeight());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getTableHeader().setReorderingAllowed(false);
        table.setShowGrid(false);
        table.setRowMargin(0);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                enableActions();
            }
        });
        updateColumns();
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && viewAction.isEnabled()) {
                    viewAction.actionPerformed(null);
                }
            }
        });

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTableHeader h = (JTableHeader) e.getSource();
                TableColumnModel columnModel = h.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = columnModel.getColumn(viewColumn).getModelIndex();
                if (column != -1) {
                    tableModel.setSortColumn(column);
                    updateColumns();
                }
            }
        });

        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(table.getBackground());
        viewAction.setEnabled(false);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.add(new JLabel("Usage of "));
        JLabel headerLabel = new JLabel(searchResource.getBrowserText());
        headerLabel.setIcon(ProtegeUI.getIcon(searchResource));
        northPanel.add(headerLabel);

        addButton(refreshAction);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(BorderLayout.NORTH, northPanel);
        mainPanel.add(BorderLayout.CENTER, scrollPane);
        setCenterComponent(mainPanel);
    }


    public static FindUsagePanel create(RDFResource searchInstance) {
        Collection items = FindUsage.getItems(searchInstance);
        return new FindUsagePanel(searchInstance, items);
    }


    public void dispose() {
        owlModel.removeModelListener(listener);
        tableModel.dispose();
    }


    private void enableActions() {
        viewAction.setEnabled(table.getSelectedRow() >= 0);
    }


    public Icon getIcon() {
        return OWLIcons.getFindUsageIcon();
    }


    public String getTabName() {
        return "Usage of " + searchResource.getBrowserText();
    }


    private void refresh() {
        tableModel.setItems(FindUsage.getItems(searchResource));
    }


    private void updateColumns() {
        table.getColumnModel().getColumn(COL_HOST).setCellRenderer(new ResourceRenderer());
        table.getColumnModel().getColumn(COL_USAGE).setCellRenderer(new ResourceRenderer());
        table.getColumnModel().getColumn(COL_HOST).setPreferredWidth(60);
        TableColumn typeColumn = table.getColumnModel().getColumn(COL_TYPE);
        typeColumn.setPreferredWidth(64);
        typeColumn.setMaxWidth(64);
        table.getColumnModel().getColumn(COL_USAGE).setPreferredWidth(300);
    }


    private void viewSelectedHostCls() {
        int row = table.getSelectedRow();
        RDFResource usage = tableModel.getUsage(row);
        if (usage instanceof OWLAnonymousClass) {
            showHostResource(usage);
        }
        else {
            RDFResource hostResource = tableModel.getHost(row);
            showHostResource(hostResource);
        }
    }
}
