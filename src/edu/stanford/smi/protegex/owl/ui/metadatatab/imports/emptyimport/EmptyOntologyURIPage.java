package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.emptyimport;

import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.metadatatab.OntologyURIPanel;
import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizard;
import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizardPage;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.net.URI;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 5, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class EmptyOntologyURIPage extends OWLWizardPage {

    private OntologyURIPanel ontologyURIPanel;

    private OWLModel owlModel;


    public EmptyOntologyURIPage(OWLWizard wizard, OWLModel owlModel) {
        super("Specify Ontology URI", wizard);
        this.owlModel = owlModel;
        createUI();
        setPageComplete(validateData());
    }


    private void createUI() {
        ontologyURIPanel = new OntologyURIPanel(false, false);
        ontologyURIPanel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setPageComplete(validateData());
            }
        });
        getContentComponent().add(ontologyURIPanel, BorderLayout.NORTH);
        setHelpText("Specifying the imported ontology URI", HELP_TEXT);
    }


    private boolean validateData() {
        if (ontologyURIPanel.getOntologyURI() != null) {
            URI uri = ontologyURIPanel.getOntologyURI();
            return uri != null;
        }
        else {
            return false;
        }
    }


    public WizardPage getNextPage() {
        return new LocalFileWizardPage(getOWLWizard(), owlModel);
    }


    public URI getOntologyURI() {
        return ontologyURIPanel.getOntologyURI();
    }


    public void onFinish() {
        super.onFinish();
        ((EmptyImportWizard) getWizard()).setOntologyURI(getOntologyURI());
    }


    private static final String HELP_TEXT = "<p>Please specify the URI of the new empty " +
            "ontology.</p>" +
            "<p>In general, the URI " +
            "should be an http URL that points to the location on the " +
            "web where the new ontology will eventually be made available.</p>";
}

