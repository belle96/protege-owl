package edu.stanford.smi.protegex.owl.ui.search.finder;

import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.SelectableList;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.results.HostResourceDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         19-Oct-2005
 */
public class FindResultsListView extends AbstractFindResultsView {

    private SelectableList list;

    protected FindResultsListView(ResultsViewModelFind find, HostResourceDisplay hrd) {
        super(hrd);

        setLayout(new BorderLayout(6, 6));

        list = new SelectableList();
        list.setModel(find);

        list.setCellRenderer(ResourceRenderer.createInstance());

        final JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setBackground(list.getBackground());

        add(scrollPane, BorderLayout.CENTER);
    }

    public RDFResource getSelectedResource() {
        return (RDFResource) CollectionUtilities.getFirstItem(list.getSelection());
    }

    public void addMouseListener(MouseListener l) {
        list.addMouseListener(l);
    }

    public void addKeyListener(KeyListener l) {
        list.addKeyListener(l);
    }

    public void requestFocus() {
        list.requestFocus();
        list.setSelectedIndex(0);
    }
}
