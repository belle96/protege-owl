package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import edu.stanford.smi.protege.util.ExtensionFilter;
import edu.stanford.smi.protege.util.FileField;
import edu.stanford.smi.protege.util.WizardPage;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 1, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class FileImportPage extends AbstractImportStartWizardPage {

    private FileField fileField;

    public FileImportPage(ImportWizard wizard) {
        super("File import page", wizard);
        createUI();
    }


    private void createUI() {
	    setHelpText("Importing an ontology contained in a specific file", HELP_TEXT);	    
	    java.util.List<String> extensions = (java.util.List<String>) Arrays.asList((new String[]{"owl" , "rdfs", "rdf"}));
	    ExtensionFilter extensionFilter = new ExtensionFilter(extensions.iterator(), "Select OWL or RDF files");
        fileField = new FileField("Specify file path", "", extensionFilter);
        fileField.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setPageComplete(validateData());
            }
        });
        getContentComponent().add(fileField, BorderLayout.NORTH);
        setPageComplete(validateData());
    }


    private boolean validateData() {
        File f = fileField.getFilePath();
        if (f != null) {
            if (f.isFile()) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }


	public void nextPressed() {
		addImportedOntology();
	}

	private void addImportedOntology() {
		File file = fileField.getFilePath();
		getImportWizard().getImportData().addImportEntry(new FileImportEntry(file));
	}



    public WizardPage getNextPage() {
        return new ImportVerificationPage(getImportWizard());
    }



    private static final String HELP_TEXT = "<p>Please specify the local file that contains the " +
            "ontology to be imported.</p>";

}

