package edu.stanford.smi.protegex.owl.ui.matrix;

import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.table.TableCellEditor;

/**
 * A MatrixColumn that can be edited by the user.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface EditableMatrixColumn extends MatrixColumn {

    boolean isCellEditable(RDFResource instance);


    TableCellEditor getTableCellEditor();


    /**
     * Called when editing has been finished.
     *
     * @param instance
     * @param value
     */
    void setValueAt(RDFResource instance, Object value);
}
