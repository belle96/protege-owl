package edu.stanford.smi.protegex.owl.ui.matrix.property;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixColumn;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixFilter;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyMatrixTableModel extends MatrixTableModel {

    public PropertyMatrixTableModel(OWLModel owlModel, MatrixFilter filter) {
        super(owlModel, filter);
    }


    public RDFProperty getRDFSlotCls(int row) {
        return (RDFProperty) getInstance(row);
    }


    protected void addDefaultColumns() {
        super.addDefaultColumns();
        addColumn(new PropertyRangeMatrixColumn());
        addColumn(new PropertyDomainMatrixColumn());
        addColumn(new InversePropertyMatrixColumn());
        addColumn(new PropertyCharacteristicsMatrixColumn());
    }


    public int getNewColumnIndex(MatrixColumn col) {
        return super.getNewColumnIndex(col) - 4;
    }
}
