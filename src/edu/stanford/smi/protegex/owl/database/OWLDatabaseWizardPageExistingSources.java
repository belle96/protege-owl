package edu.stanford.smi.protegex.owl.database;

import edu.stanford.smi.protege.util.Wizard;

public class OWLDatabaseWizardPageExistingSources extends OWLDatabaseWizardPage {
    
    private boolean fileToDatabase = false;

    public OWLDatabaseWizardPageExistingSources(Wizard wizard, OWLDatabasePlugin plugin) {
        super(wizard, plugin);
    }

    @Override
    public boolean getFromExistingSources() {
        return true;
    }
    
    public boolean isFileToDatabase() {
        return fileToDatabase;
    }

    public void setFileToDatabase(boolean sourcesAreFileSource) {
        this.fileToDatabase = sourcesAreFileSource;
    }
}
