package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.WizardPage;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 1, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class URLImportPage extends AbstractImportStartWizardPage {

    private JTextField urlField;


    public URLImportPage(ImportWizard wizard) {
        super("URL Import Page", wizard);
        createUI();
    }


    private void createUI() {
        setHelpText("Importing an ontology from the web", HELP_TEXT);
        urlField = new JTextField();
        urlField.setColumns(40);
        urlField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                setPageComplete(validateData());
            }


            public void removeUpdate(DocumentEvent e) {
                setPageComplete(validateData());
            }


            public void changedUpdate(DocumentEvent e) {
                setPageComplete(validateData());
            }
        });
        LabeledComponent lc = new LabeledComponent("Ontology URL (http://...)", urlField);
        getContentComponent().add(lc, BorderLayout.NORTH);
        setPageComplete(validateData());
    }


	public void nextPressed() {
		try {
			URL url = new URL(urlField.getText().trim());
			getImportWizard().getImportData().addImportEntry(new URLImportEntry(url));
		}
		catch(MalformedURLException e) {
                  Log.getLogger().log(Level.SEVERE, "Exception caught", e);
		}
	}


    private boolean validateData() {
        if (urlField.getText().trim().length() == 0) {
            return false;
        }
        try {
	        // The URL represents the URL of the document, not the ontology!
            URL url = new URL(urlField.getText().trim());
            return url.getHost().length() > 0 && url.getProtocol() != null && url.getProtocol().equals("http");
        }
        catch (MalformedURLException e) {
            return false;
        }


    }


    public WizardPage getNextPage() {
        return new ImportVerificationPage(getImportWizard());
    }


    private static final String HELP_TEXT = "<p>Please specify the URL that points to the document " +
            "containing the ontology to be imported. The URL can " +
            "be specified by typing it into the above text field, " +
            "or it could be copied and pasted from a web browser " +
            "for example.</p>";
}

