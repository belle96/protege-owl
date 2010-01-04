package edu.stanford.smi.protegex.owl.ui.repository.wizard.impl;

import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.ui.repository.wizard.RepositoryCreatorWizardPanel;

import java.awt.*;
import java.io.File;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 26, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class FileSelectionWizardPanel extends RepositoryCreatorWizardPanel {

    private FileBrowserPanel fileBrowserPanel;

    private WizardPage wizardPage;


    public FileSelectionWizardPanel(WizardPage wizardPage, boolean directoriesOnly, String helpText) {
        this.wizardPage = wizardPage;
        setLayout(new BorderLayout(12, 12));
        add(fileBrowserPanel = new FileBrowserPanel(wizardPage,
                directoriesOnly ? "Folder name" : "File name",
                directoriesOnly,
                helpText));

    }


    public abstract Repository createRepository(File f, boolean forceReadOnly, boolean recursive);


    public Repository createRepository() {
        return createRepository(fileBrowserPanel.getFile(), fileBrowserPanel.isForceReadOnly(),
                                fileBrowserPanel.isRecursive());
    }


    public boolean validateData() {
        return fileBrowserPanel.validateFileName();
    }
}

