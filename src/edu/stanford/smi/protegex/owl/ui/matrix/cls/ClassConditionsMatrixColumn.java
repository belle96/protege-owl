package edu.stanford.smi.protegex.owl.ui.matrix.cls;

import edu.stanford.smi.protegex.owl.ui.matrix.AbstractMatrixColumn;

import javax.swing.table.TableCellRenderer;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassConditionsMatrixColumn extends AbstractMatrixColumn {

    public ClassConditionsMatrixColumn() {
        super("Conditions", 650);
    }


    public TableCellRenderer getCellRenderer() {
        return new ConditionsCellRenderer();
    }
}
