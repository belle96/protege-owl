package edu.stanford.smi.protegex.owl.ui.matrix;

import javax.swing.table.TableCellRenderer;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface MatrixColumn {

    TableCellRenderer getCellRenderer();


    String getName();


    int getWidth();
}
