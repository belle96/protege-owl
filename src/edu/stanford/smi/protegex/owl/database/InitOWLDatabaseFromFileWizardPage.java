package edu.stanford.smi.protegex.owl.database;

import java.awt.BorderLayout;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.stanford.smi.protege.util.URIField;
import edu.stanford.smi.protege.util.Wizard;
import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InitOWLDatabaseFromFileWizardPage extends WizardPage {

    private CreateOWLDatabaseFromFileProjectPlugin plugin;

    private URIField uriField;


    public InitOWLDatabaseFromFileWizardPage(Wizard wizard, CreateOWLDatabaseFromFileProjectPlugin plugin) {
        super("Specify Ontology File", wizard);
        this.plugin = plugin;
        uriField = new URIField("URI of OWL file to populate the new database with",
                null, ".owl", "OWL Files");
        uriField.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setPageComplete(true);
            }
        });
        add(BorderLayout.CENTER, uriField);
        add(BorderLayout.SOUTH,
                OWLUI.createHelpPanel("This allows you to use the Protege-OWL parser to load OWL/RDF files directly into a new database.  In theory, this parser should be able to handle files of arbitrary size, because it loads the file triple-by-triple.", "Create OWL Database from a File"));
        setPageComplete(false);
    }


    @Override
    public WizardPage getNextPage() {
        OWLDatabaseWizardPageExistingSources wizard = new OWLDatabaseWizardPageExistingSources(getWizard(), plugin);
        wizard.setFileToDatabase(true);
        return wizard;
    }


    @Override
    public void onFinish() {
        plugin.setOntologyInputSource(uriField.getAbsoluteURI());
    }
}
