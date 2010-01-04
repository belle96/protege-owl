package edu.stanford.smi.protegex.owl.ui.metadatatab;

import edu.stanford.smi.protege.util.Wizard;
import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.jena.OWLFilesPlugin;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfileSelectionWizardPage;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 27, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OntologyURIWizardPage extends WizardPage {

    private OWLFilesPlugin plugin;

    private OntologyURIPanel ontologyURIPanel;


    public OntologyURIWizardPage(Wizard wizard, OWLFilesPlugin plugin) {
        super("Ontology URI Page", wizard);
        this.plugin = plugin;
        createUI();
    }


    private void createUI() {
        setLayout(new BorderLayout());
        add(ontologyURIPanel = new OntologyURIPanel(true, true));
        ontologyURIPanel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setPageComplete(ontologyURIPanel.getOntologyURI() != null);
            }
        });
        setPageComplete(ontologyURIPanel.getOntologyURI() != null);
    }


    public void onFinish() {
        if (plugin != null) {
            plugin.setOntologyName(ontologyURIPanel.getOntologyURI().toString());
        }
    }


    public WizardPage getNextPage() {
        return new ProfileSelectionWizardPage(getWizard(), plugin);
    }
}

