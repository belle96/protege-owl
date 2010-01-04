package edu.stanford.smi.protegex.owl.ui.testing;

import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.testing.OWLTest;
import edu.stanford.smi.protegex.owl.testing.OWLTestLibrary;
import edu.stanford.smi.protegex.owl.testing.OWLTestManager;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * A JPanel to specify which OWLTests shall be active in a project.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLTestSettingsPanel extends JPanel {

    private JCheckBox automaticTestCheckBox;

    private JTable table;

    private OWLTestSettingsTableModel tableModel;


    public OWLTestSettingsPanel(final OWLTestManager testManager) {

        tableModel = new OWLTestSettingsTableModel(testManager);
        table = new JTable(tableModel) {
            public String getToolTipText(MouseEvent event) {
                return getDocumentationOfTestAt(event.getY());
            }
        };
        table.setToolTipText("");

        LabeledComponent tablePanel = new LabeledComponent("Available Tests", new JScrollPane(table));
        tablePanel.setPreferredSize(new Dimension(600, 400));
        table.getColumnModel().getColumn(OWLTestSettingsTableModel.COL_ACTIVATED).setMaxWidth(80);

        automaticTestCheckBox = new JCheckBox("Repair continuously", testManager.isAutoRepairEnabled());
        automaticTestCheckBox.setToolTipText("<HTML>If activated, then all repairable tests will be executed after each relevant change (until now: Only for changes in property characteristics).</HTML>");
        automaticTestCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testManager.setAutoRepairEnabled(automaticTestCheckBox.isSelected());
            }
        });

        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, tablePanel);
        add(BorderLayout.SOUTH, automaticTestCheckBox);
    }


    private String getDocumentationOfTestAt(int y) {
        int row = y / table.getRowHeight();
        if (row >= 0 && row < tableModel.getRowCount()) {
            if (!tableModel.isSeparator(row)) {
                Class clazz = tableModel.getOWLTestClass(row);
                OWLTest test = OWLTestLibrary.getOWLTest(clazz);
                String documentation = test.getDocumentation();
                return documentation;
            }
        }
        return null;
    }


    public static void showOWLTestSettingsDialog(OWLModel owlModel) {
        OWLTestSettingsPanel panel = new OWLTestSettingsPanel(owlModel);
        ProtegeUI.getModalDialogFactory().showDialog(ProtegeUI.getTopLevelContainer(owlModel.getProject()), panel,
                "Test Settings", ModalDialogFactory.MODE_CLOSE);
    }
}
