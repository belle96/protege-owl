package edu.stanford.smi.protegex.owl.ui.matrix;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractMatrixColumn implements MatrixColumn {

    private String name;

    private int width;


    protected AbstractMatrixColumn(String name, int width) {
        this.name = name;
        this.width = width;
    }


    public TableCellRenderer getCellRenderer() {
        return new DefaultTableCellRenderer();
    }


    public String getName() {
        return name;
    }


    public int getWidth() {
        return width;
    }
}
