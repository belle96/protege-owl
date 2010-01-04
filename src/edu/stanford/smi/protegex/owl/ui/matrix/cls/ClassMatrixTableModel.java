package edu.stanford.smi.protegex.owl.ui.matrix.cls;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixColumn;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixFilter;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassMatrixTableModel extends MatrixTableModel {

    public ClassMatrixTableModel(OWLModel owlModel, MatrixFilter filter) {
        super(owlModel, filter);
    }


    public RDFSNamedClass getCls(int row) {
        return (RDFSNamedClass) getInstance(row);
    }


    protected void addDefaultColumns() {
        super.addDefaultColumns();
        addColumn(new ClassConditionsMatrixColumn());
    }


    public int getNewColumnIndex(MatrixColumn col) {
        return super.getNewColumnIndex(col) - 1;
    }
}
