package edu.stanford.smi.protegex.owl.ui.triplestore;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.URI;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.URIField;
import edu.stanford.smi.protege.util.Validatable;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStoreModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AddTripleStorePanel extends JPanel implements Validatable {

    private OWLModel owlModel;

    private JTextField prefixField;

    protected URIField uriField;


    protected AddTripleStorePanel(OWLModel owlModel) {
        this(owlModel, "Prefix");
    }


    protected AddTripleStorePanel(OWLModel owlModel, String prefixTitle) {
        this.owlModel = owlModel;
        try {
            URI uri = new URI("http://www.owl-ontologies.com/submodel.owl");
            uriField = new URIField("Ontology name (URI)", uri, ".owl", "");
            prefixField = new JTextField("submodel");
            JPanel mainPanel = new JPanel(new GridLayout(3, 1));
            mainPanel.add(uriField);
            mainPanel.add(new LabeledComponent(prefixTitle, prefixField));
            setLayout(new BorderLayout(0, 16));
            add(BorderLayout.CENTER, mainPanel);
        }
        catch (Exception ex) {
          Log.getLogger().log(Level.SEVERE, "Exception caught", ex);
        }
    }


    protected String getNamespaceFromURI(URI uri) {
        String namespace = uri.toString();
        return Jena.getNamespaceFromURI(namespace);
    }


    protected OWLModel getOWLModel() {
        return owlModel;
    }


    protected String getPrefix() {
        return prefixField.getText();
    }


    protected URI getURI() {
        return uriField.getAbsoluteURI();
    }


    public void saveContents() {
    }


    protected void setPrefix(String prefix) {
        prefixField.setText(prefix);
    }


    public boolean validateContents() {
        URI uri = getURI();
        if (uri != null) {
            String uriString = uri.toString();
            TripleStoreModel tsm = owlModel.getTripleStoreModel();
            if (tsm.getTripleStore(uriString) == null) {
                String prefix = getPrefix();
                if (prefix != null) {
                    final String namespace = owlModel.getNamespaceManager().getNamespaceForPrefix(prefix);
                    final String nuri = getNamespaceFromURI(uri);
                    if (namespace == null || namespace.equals(nuri)) {
                        return true;
                    }
                    else {
                        ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel,
                                "Prefix \"" + prefix + "\" is already used.");
                    }
                }
                else {
                    ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel,
                            "Invalid Prefix.");
                }
            }
            else {
                ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel,
                        "Model with this URI already exists:\n" + uriString);
            }
        }
        else {
            ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel,
                    "Invalid URI.");
        }
        return false;
    }
}
