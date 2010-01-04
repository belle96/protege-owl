package edu.stanford.smi.protegex.owl.ui.triplestore;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStoreUtil;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.RepositoryManager;

import javax.swing.table.AbstractTableModel;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
class TripleStoreTableModel extends AbstractTableModel {

    public final static int COL_EDITABLE = 0;

    public final static int COL_ACTIVE = 1;

    public final static int COL_URI = 2;

    public final static int COL_COUNT = 3;

    private OWLModel owlModel;

    public static final String MAIN_FILE_NAME = "<Main File>";


    TripleStoreTableModel(OWLModel owlModel) {
        this.owlModel = owlModel;
    }


    public int getColumnCount() {
        return COL_COUNT;
    }


    public Class getColumnClass(int columnIndex) {
        if (columnIndex == COL_ACTIVE || columnIndex == COL_EDITABLE) {
            return Boolean.class;
        }
        else {
            return String.class;
        }
    }


    public String getColumnName(int column) {
        if (column == COL_ACTIVE) {
            return "Active";
        }
        else if (column == COL_URI) {
            return "URI";
        }
        else if (column == COL_EDITABLE) {
            return "Editable";
        }
        else {
            return null;
        }
    }


    public int getRowCount() {
        return owlModel.getTripleStoreModel().getTripleStores().size() - 1;
    }


    public int getSelectedTripleStoreRow() {
        for (int i = 0; i < getRowCount(); i++) {
            if (Boolean.TRUE.equals(getValueAt(i, COL_ACTIVE))) {
                return i;
            }
        }
        return -1;
    }


    TripleStore getTripleStore(int row) {
        return (TripleStore) owlModel.getTripleStoreModel().getTripleStores().get(row + 1);
    }


    public Object getValueAt(int rowIndex, int columnIndex) {
        TripleStore tripleStore = getTripleStore(rowIndex);
        String uri = tripleStore.getName();
        if (columnIndex == COL_ACTIVE) {
            return Boolean.valueOf(tripleStore == owlModel.getTripleStoreModel().getActiveTripleStore());
        }
        else if (columnIndex == COL_URI) {
            if (rowIndex == 0) {
                return "<Main ontology>";
            }
            else {
                return uri;
            }
        }
        else if (columnIndex == COL_EDITABLE) {
            if (rowIndex == 0) {
                return Boolean.TRUE;
            }
            else {
                try {
                    RepositoryManager man = owlModel.getRepositoryManager();
                    URI ontURI = new URI(uri);
                    Repository rep = man.getRepository(ontURI);
                    if (rep != null) {
                        return Boolean.valueOf(rep.isWritable(ontURI));
                    }
                    else {
                        return Boolean.FALSE;
                    }
                }
                catch (URISyntaxException e) {
                    return Boolean.FALSE;
                }
            }
        }
        else {
            return null;
        }
    }


    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == COL_ACTIVE) {
            return Boolean.TRUE.equals(getValueAt(rowIndex, COL_EDITABLE));
        }
        else if (columnIndex == COL_EDITABLE) {
            return false;
        }
        else {
            return false;
        }
    }


    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == COL_ACTIVE) {
            if (Boolean.TRUE.equals(aValue)) {
                TripleStore tripleStore = getTripleStore(rowIndex);
                TripleStoreUtil.switchTripleStore(owlModel, tripleStore);
                for (int row = 0; row < getRowCount(); row++) {
                    fireTableCellUpdated(row, columnIndex);
                }
            }
        }
    }
}
