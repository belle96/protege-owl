package edu.stanford.smi.protegex.owl.ui.repository.wizard.impl;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.impl.HTTPRepository;
import edu.stanford.smi.protegex.owl.repository.util.OntologyNameExtractor;
import edu.stanford.smi.protegex.owl.repository.util.URLInputSource;
import edu.stanford.smi.protegex.owl.ui.repository.wizard.RepositoryCreatorWizardPanel;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 3, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class HTTPRepositoryCreatorWizardPanel extends RepositoryCreatorWizardPanel {

    private JTextField urlField;

    private WizardPage wizardPage;


    public HTTPRepositoryCreatorWizardPanel(WizardPage wizardPage) {
        this.wizardPage = wizardPage;
        createUI();
    }


    private void createUI() {
        urlField = new JTextField();
        urlField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                wizardPage.setPageComplete(validateData());
            }


            public void removeUpdate(DocumentEvent e) {
                wizardPage.setPageComplete(validateData());
            }


            public void changedUpdate(DocumentEvent e) {
                wizardPage.setPageComplete(validateData());
            }
        });
        LabeledComponent lc = new LabeledComponent("URL (http:// address)", urlField);
        setLayout(new BorderLayout(12, 12));
        add(lc, BorderLayout.NORTH);
        add(OWLUI.createHelpPanel(HELP_TEXT, null, OWLUI.WIZARD_HELP_HEIGHT),
                BorderLayout.SOUTH);
        wizardPage.setPageComplete(validateData());
    }


    public Repository createRepository() {
        if (urlField.getText().length() > 0) {
            String errorMessage = null;
            try {
                URL url = new URL(urlField.getText());
                OntologyNameExtractor extractor = new OntologyNameExtractor(new URLInputSource(url));
                extractor.getOntologyName();
                if (extractor.isPossiblyValidOntology()) {
                    return new HTTPRepository(url);
                }
                else {
                    errorMessage = "The document pointed to by " + url + " does not " +
                            "appear to be a valid ontology.";
                }
            }
            catch (MalformedURLException e) {
                errorMessage = "The URL " + urlField.getText() + " is malformed: " +
                        e.getMessage();
            }
            catch (IOException ioEx) {
                errorMessage = "Could not obtain ontology from " + urlField.getText() + " due " +
                        "to an IO error: " + ioEx.getMessage();
            }
            JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        else {
            return null;
        }
    }


    public boolean validateData() {
        if (urlField.getText().length() > 0) {
            try {
                URL url = new URL(urlField.getText());
                boolean b = url.getProtocol().equals("http") &&
                        url.getHost().length() > 0 &&
                        url.getPath().length() > 0;
                return b;
            }
            catch (MalformedURLException e) {
                return false;
            }
        }
        else {
            return false;
        }
    }


    private static final String HELP_TEXT = "<p>Please specify a URL (http address) that points to the " +
            "location on the web where the ontology can " +
            "be downloaded from.</p>";
}

