package edu.stanford.smi.protegex.owl.ui.metadatatab.prefixes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.NamespaceManagerAdapter;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.impl.AbstractNamespaceManager;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * A TableModel for editing prefixes in an OWLModel.
 * The resulting table has two columns, one for prefixes and one for the namespace.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PrefixesTableModel extends AbstractTableModel  {
    private static final long serialVersionUID = 4288139995372553605L;

    private OWLOntology ontology;

    public final static int COL_PREFIX = 0;

    public final static int COL_NAMESPACE = 1;

    public final static int COL_COUNT = 2;




    /**
     * A list of prefixes (one String value for each row)
     */
    private List<String> prefixes = new ArrayList<String>();


    public PrefixesTableModel(OWLOntology ontology) {
        this.ontology = ontology;
        fill();
        getNamespaceManager().addNamespaceManagerListener(new NamespaceManagerAdapter() {
           @Override
           public void namespaceChanged(String prefix, String oldValue, String newValue) {
               fill();
               fireTableDataChanged();
           }
        });
    }

    private void fill() {
        prefixes = new ArrayList<String>(getNamespaceManager().getPrefixes());
        Collections.sort(prefixes);
    }


    public int getColumnCount() {
        return COL_COUNT;
    }


    @Override
	public Class getColumnClass(int columnIndex) {
        return String.class;
    }


    @Override
	public String getColumnName(int column) {
        if (column == COL_PREFIX) {
            return "Prefix";
        }
        else if (column == COL_NAMESPACE) {
            return "Namespace";
        }
        else {
            return null;
        }
    }


    public String getNamespace(int rowIndex) {
        String prefix = getPrefix(rowIndex);
        return getNamespaceManager().getNamespaceForPrefix(prefix);
    }


    NamespaceManager getNamespaceManager() {
        return ontology.getOWLModel().getNamespaceManager();
    }


    public String getPrefix(int row) {
        return prefixes.get(row);
    }


    int getPrefixRow(String prefix) {
        return prefixes.indexOf(prefix);
    }


    public int getRowCount() {
        return prefixes.size();
    }


    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == COL_PREFIX) {
            return getPrefix(rowIndex);
        }
        else if (columnIndex == COL_NAMESPACE) {
            return getNamespace(rowIndex);
        }
        else {
            return null;
        }
    }


    @Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
        String prefix = getPrefix(rowIndex);
        return isPrefixEditable(prefix);
    }


    public boolean isNamespaceEditable(String namespace) {
        String prefix = getNamespaceManager().getPrefix(namespace);
        return isPrefixEditable(prefix);
    }


    boolean isPrefixEditable(String prefix) {
        return getNamespaceManager().isModifiable(prefix);
    }


    @Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        NamespaceManager nsm = getNamespaceManager();
        if (columnIndex == COL_PREFIX) {
            String value = (String) aValue;
            if (nsm.getNamespaceForPrefix(value) == null) {
                if (value.equals(AbstractNamespaceManager.DEFAULT_NAMESPACE_PREFIX)
                        || AbstractNamespaceManager.isValidPrefix(value)) {
                    String namespace = getNamespace(rowIndex);
                    setPrefixOfNamespace(namespace, value);
                }
            }
        }
        else if (columnIndex == COL_NAMESPACE) {
            String value = (String) aValue;
            String defaultPrefix = nsm.getDefaultNamespace();
            if (nsm.getPrefix(value) == null && (defaultPrefix == null || !defaultPrefix.equals(value))) {
                if (Jena.isNamespaceWithSeparator(value)) {
                    String prefix = getPrefix(rowIndex);
                    setNamespaceOfPrefix(prefix, value);
                }
            }
        }
        fill();
        fireTableDataChanged();
    }


    private void setNamespaceOfPrefix(String prefix, String value) {
        OWLModel owlModel = ontology.getOWLModel();
        try {
            owlModel.beginTransaction("Change namespace of " + prefix + " to " + value, ontology.getName());
            owlModel.getNamespaceManager().setPrefix(value, prefix);
            owlModel.commitTransaction();
        }
        catch (Exception ex) {
        	owlModel.rollbackTransaction();
            OWLUI.handleError(owlModel, ex);
        }
    }


    private void setPrefixOfNamespace(String namespace, String value) {
        OWLModel owlModel = ontology.getOWLModel();
        try {
            owlModel.beginTransaction("Change prefix of " + namespace + " to " + value, ontology.getName());
            owlModel.getNamespaceManager().setPrefix(namespace, value);
            owlModel.commitTransaction();
        }
        catch (Exception ex) {
        	owlModel.rollbackTransaction();
            OWLUI.handleError(owlModel, ex);
        }
    }

    public int getRowOfPrefix(String prefix) {
        return prefixes.indexOf(prefix);
    }
}
