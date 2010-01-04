package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.impl.AbstractNamespaceManager;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 30, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class AddedPrefixesTableModel extends AbstractTableModel {

    private NamespaceManager manager;

    private ArrayList prefixes;

    public static final int PREFIX_COLUMN = 0;

    public static final int NAMESPACE_COLUMN = 1;

    private static final String [] COLUMN_NAMES = new String []{"Prefix", "Namespace"};


    public AddedPrefixesTableModel(NamespaceManager manager, Collection addedPrefixes) {
        this.manager = manager;
        prefixes = new ArrayList(addedPrefixes);
    }


    public int getRowCount() {
        return prefixes.size();
    }


    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }


    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }


    public Object getValueAt(int rowIndex,
                             int columnIndex) {
        if (columnIndex == PREFIX_COLUMN) {
            return prefixes.get(rowIndex);
        }
        else {
            return manager.getNamespaceForPrefix((String) prefixes.get(rowIndex));
        }
    }


    public boolean isCellEditable(int rowIndex,
                                  int columnIndex) {
        return columnIndex == PREFIX_COLUMN;
    }


    public void setValueAt(Object aValue,
                           int rowIndex,
                           int columnIndex) {
        if (columnIndex == PREFIX_COLUMN) {
            if (manager.getNamespaceForPrefix((String) aValue) == null &&
                    AbstractNamespaceManager.isValidPrefix((String) aValue)) {
                String namespace = (String) getValueAt(rowIndex, NAMESPACE_COLUMN);
                manager.setPrefix(namespace, (String) aValue);
                prefixes.set(rowIndex, aValue);
                fireTableRowsUpdated(rowIndex, rowIndex);
            }
        }
    }


}

