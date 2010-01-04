package edu.stanford.smi.protegex.owl.ui.metadatatab;

import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.ui.metadatatab.prefixes.PrefixesPanel;

import javax.swing.*;
import java.awt.*;

/**
 * A JPanel that allows users to view and edit the default namespace, the prefixes
 * and the imports of an OWLOntology.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLNamespacesPanel extends JPanel implements Disposable {

    private PrefixesPanel prefixesPanel;


    public OWLNamespacesPanel(OWLOntology ontology) {
        prefixesPanel = new PrefixesPanel(ontology);
        setLayout(new BorderLayout(0, 10));
        add(BorderLayout.NORTH, new LabeledComponent("Default Namespace",
                                                     prefixesPanel.getDefaultNamespaceField()));
        add(BorderLayout.CENTER, prefixesPanel);
    }


    public void dispose() {
        prefixesPanel.dispose();
    }


    public void setEnabled(boolean enabled) {        
        prefixesPanel.setEnabled(enabled);
        super.setEnabled(enabled);
    }
}
