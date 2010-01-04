package edu.stanford.smi.protegex.owl.ui.triplestore;

import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * A JPanel that can be used to switch the active TripleStore in an OWLModel.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TripleStoreSelectionPanel extends JPanel {

    private OWLModel owlModel;

    private JTable table;

    private TripleStoreTableModel tableModel;


    public TripleStoreSelectionPanel(OWLModel owlModel) {
        this.owlModel = owlModel;
        tableModel = new TripleStoreTableModel(owlModel);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout(0, 16));
        LabeledComponent lc = new LabeledComponent("Ontologies", scrollPane);
        add(BorderLayout.CENTER, lc);
        setPreferredSize(new Dimension(700, Math.min(700,
                tableModel.getRowCount() * table.getRowHeight() + 240)));
        scrollPane.getViewport().setBackground(table.getBackground());
        setColumnWidth(TripleStoreTableModel.COL_ACTIVE, 50);
        setColumnWidth(TripleStoreTableModel.COL_URI, 400);
        setColumnWidth(TripleStoreTableModel.COL_EDITABLE, 50);
        Component helpPanel = OWLUI.createHelpPanel(HELP_TEXT, "What is the Active Ontology?");
        add(BorderLayout.SOUTH, helpPanel);
    }


    private void setColumnWidth(int column, int width) {
        TableColumn col = table.getColumnModel().getColumn(column);
        //col.setMinWidth(width);
        col.setPreferredWidth(width);
    }


    public static void showDialog(OWLModel owlModel) {
        TripleStoreSelectionPanel panel = new TripleStoreSelectionPanel(owlModel);
        ProtegeUI.getModalDialogFactory().showDialog(ProtegeUI.getTopLevelContainer(owlModel.getProject()), panel,
                "Active Ontology", ModalDialogFactory.MODE_CLOSE);
    }


    private final static String HELP_TEXT =
            "<p>If the main ontology imports other ontologies, this panel indicates whether or not the " +
                    "various imports are editable. If an imported ontology is editable then it can be set to " +
                    "be the 'Active Ontology', which is the ontology that any edits are applied to.</p>" +
                    "<p>Whether or not an imported ontology is editable depends on where the ontology was " +
                    "imported from.  For example, ontologies that are imported from the local file system " +
                    "will probably be editable, where as ontologies that are imported from the web will not " +
                    "be editable.</p>";
}
