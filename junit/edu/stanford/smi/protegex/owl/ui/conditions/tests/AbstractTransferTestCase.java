package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTable;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableTransferHandler;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
abstract class AbstractTransferTestCase extends AbstractConditionsTableTestCase {

    protected void transferRow(ConditionsTableModel tableModel, int from, int to, int action) {
        ConditionsTable table = new ConditionsTable(owlModel, tableModel);
        ConditionsTableTransferHandler th = (ConditionsTableTransferHandler) table.getTransferHandler();
        table.setSelectedRow(from);
        MouseEvent dragEvent = new MouseEvent(table,
                MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(),
                MouseEvent.BUTTON1_MASK, 10, table.getRowHeight() * from + 4, 1, false, 1);
        th.exportAsDrag(table, dragEvent, action);
        table.setSelectedRow(to);
        th.importData(table, th.recentTransferable);
        if (action == TransferHandler.MOVE) {
            th.cleanup(table, true);
        }
    }
}
