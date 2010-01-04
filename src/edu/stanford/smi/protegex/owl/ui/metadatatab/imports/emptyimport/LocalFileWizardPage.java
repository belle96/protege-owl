package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.emptyimport;

import edu.stanford.smi.protege.util.FileField;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizard;
import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizardPage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
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
public class LocalFileWizardPage extends OWLWizardPage {

    private File localFile;

    private OWLModel owlModel;


    public LocalFileWizardPage(OWLWizard wizard, OWLModel owlModel) {
        super("Local File", wizard);
        this.owlModel = owlModel;
        createUI();
        setPageComplete(validateData());
    }


    private String getSuggestedPath() {
        String path = "";
        if (owlModel.getProject() != null) {
            URI uri = owlModel.getProject().getProjectURI();
            if (uri != null) {
                File f = new File(new File(uri).getParentFile(), "Import" + System.currentTimeMillis() / 1000 + ".owl");
                path = f.toString();
            }
        }
        return path;
    }


    private boolean validateData() {
        return localFile != null;
    }


    private void createUI() {
        final FileField fileField = new FileField("Local file", getSuggestedPath(), "owl", "OWL Files");
        fileField.setDialogType(JFileChooser.SAVE_DIALOG);
        fileField.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                localFile = fileField.getFilePath();
                setPageComplete(validateData());
            }
        });
        localFile = fileField.getFilePath();
        getContentComponent().add(fileField, BorderLayout.NORTH);
	    setHelpText("Specifying a local file", HELP_TEXT);
    }


    public void onFinish() {
        super.onFinish();
        ((EmptyImportWizard) getWizard()).setLocalFile(localFile);
    }


    private static final String HELP_TEXT = "<p>Please specify a local file where the new empty ontology " +
            "will be stored.</p>" +
            "<p>(This local file will be added to the project ontology " +
            "repository so that the import is redirected to it)</p>";
}

