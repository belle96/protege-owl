package edu.stanford.smi.protegex.owl.ui.metadatatab.imports;

import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.event.PropertyValueAdapter;
import edu.stanford.smi.protegex.owl.model.event.PropertyValueListener;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 * TT: This class does not seems to be used anymore.
 * Consider for clean-up.
 */
public class ImportsTableModel extends AbstractTableModel implements Disposable {

    public final static int COL_URI = 0;

    public final static int COL_COUNT = 1;

    private PropertyValueListener listener = new PropertyValueAdapter() {
        public void propertyValueChanged(RDFResource resource, RDFProperty property, Collection oldValues) {
            if (OWLNames.Slot.IMPORTS.equals(property.getName())) {
                refill();
            }
        }
    };


    private OWLOntology ontology;

    private List uris = new ArrayList();


    public ImportsTableModel(OWLOntology ontology) {
        this.ontology = ontology;
        ontology.addPropertyValueListener(listener);
        uris = new ArrayList(ontology.getImports());
    }


    public void addImport(String uri) {
        ontology.addImports(uri);
    }


    public void deleteRow(int row) {
        String uri = getURI(row);
        //TripleStoreModel tsm = ontology.getOWLModel().getTripleStoreModel();
        //TripleStore tripleStore = tsm.getTripleStore(uri);
        //tsm.deleteTripleStore(tripleStore);
        ontology.removeImports(uri);
    }


    public void dispose() {
        ontology.removePropertyValueListener(listener);
    }


    public Class getColumnClass(int columnIndex) {
        return String.class;
    }


    public int getColumnCount() {
        return COL_COUNT;
    }


    public String getColumnName(int column) {
        if (column == COL_URI) {
            return "Imported URI";
        }
        else {
            return null;
        }
    }


    public NamespaceManager getNamespaceManager() {
        return ontology.getOWLModel().getNamespaceManager();
    }


    OWLModel getOWLModel() {
        return ontology.getOWLModel();
    }


    public int getRowCount() {
        return uris.size();
    }


    public String getURI(int row) {
        return (String) uris.get(row);
    }


    public int getURIRow(String uri) {
        return uris.indexOf(uri);
    }


    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == COL_URI) {
            return getURI(rowIndex);
        }
        else {
            return null;
        }
    }


    private void refill() {
        uris = new ArrayList(ontology.getImports());
        fireTableDataChanged();
    }
}
