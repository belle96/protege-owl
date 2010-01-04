package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import edu.stanford.smi.protegex.owl.model.OWLMaxCardinality;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTable;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableTransferHandler;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CutRowTestCase extends AbstractConditionsTableTestCase {

    public void testCutTwice() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        cls.addSuperclass(owlModel.createOWLMinCardinality(property, 1));
        cls.addSuperclass(owlModel.createOWLMaxCardinality(property, 2));
        ConditionsTableModel tableModel = getTableModel(cls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlModel.getRootCls(),
                OWLMinCardinality.class,
                OWLMaxCardinality.class
        });
        ConditionsTable table = new ConditionsTable(owlModel, tableModel);
        ConditionsTableTransferHandler th = (ConditionsTableTransferHandler) table.getTransferHandler();
        table.setSelectedRow(4);
        try {
            th.exportToClipboard(table, table.getToolkit().getSystemClipboard(), TransferHandler.MOVE);
            assertTableModelStructure(tableModel, new Object[]{
                    SUFFICIENT,
                    NECESSARY,
                    owlModel.getRootCls(),
                    OWLMinCardinality.class
            });
        }
        catch (HeadlessException he) {
            // @todo
            // For junit tests that run in headless mode,
            // what should we do here???
        }
    }
}
