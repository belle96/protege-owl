package edu.stanford.smi.protegex.owl.ui.jena;

import edu.stanford.smi.protege.util.Wizard;
import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.jena.OWLFilesCreateProjectPlugin;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfileSelectionWizardPage;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLFilesWizardPage extends AbstractOWLFilesWizardPage {


    public OWLFilesWizardPage(Wizard wizard, OWLFilesCreateProjectPlugin aPlugin) {
        super(wizard, aPlugin, "OWL Files");
    }


    public WizardPage getNextPage() {
        return new ProfileSelectionWizardPage(getWizard(), getPlugin());
    }
}
