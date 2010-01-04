package edu.stanford.smi.protegex.owl.ui.explorer.filter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.stanford.smi.protege.ui.SlotSubslotNode;
import edu.stanford.smi.protege.util.SelectableTree;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.properties.OWLPropertySubpropertyRoot;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * A JPanel to select a valid RDFProperty for a DefaultExplorerFilter.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ValidPropertyPanel extends JPanel {

    private DefaultExplorerFilter filter;

    private JTree tree;


    public ValidPropertyPanel(OWLModel owlModel, DefaultExplorerFilter filter) {
        this.filter = filter;
        tree = new SelectableTree(null, new OWLPropertySubpropertyRoot(owlModel));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                updateFilter();
            }
        });
        tree.setCellRenderer(new ResourceRenderer());
        OWLLabeledComponent lc = new OWLLabeledComponent("Show only Restrictions on Property", new JScrollPane(tree));
        lc.addHeaderButton(new AbstractAction("Clear selection", OWLIcons.getDeleteIcon()) {
            public void actionPerformed(ActionEvent e) {
                tree.clearSelection();
            }
        });
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, lc);
        setPreferredSize(new Dimension(ValidClassesPanel.PREFERRED_WIDTH, 260));
    }


    private void updateFilter() {
        TreePath path = tree.getSelectionPath();
        if (path == null) {
            filter.setValidProperty(null);
        }
        else {
            SlotSubslotNode node = (SlotSubslotNode) path.getLastPathComponent();
            RDFProperty property = (RDFProperty) node.getUserObject();
            filter.setValidProperty(property);
        }
    }
}
