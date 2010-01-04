package edu.stanford.smi.protegex.owl.ui.repository.wizard;

import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 26, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface RepositoryCreatorWizardPlugin {

    public static final String PLUGIN_TYPE = "RepositoryCreatorWizardPlugin";


    public String getName();


    public String getDescription();


    public boolean isSuitable(OWLModel model);


    public RepositoryCreatorWizardPanel createRepositoryCreatorWizardPanel(WizardPage wizardPage, OWLModel owlModel);
}
