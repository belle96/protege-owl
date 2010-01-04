package edu.stanford.smi.protegex.owl.ui.jena;

import edu.stanford.smi.protege.util.Wizard;
import edu.stanford.smi.protegex.owl.jena.OWLFilesPlugin;

/**
 * The WizardPage used in the Protege-from-OWL create dialog.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProtegeFromOWLFilesWizardPage extends AbstractOWLFilesWizardPage {

    public ProtegeFromOWLFilesWizardPage(Wizard wizard, OWLFilesPlugin plugin) {
        super(wizard, plugin, "OWL Files (to Protege Project)");
    }
}
