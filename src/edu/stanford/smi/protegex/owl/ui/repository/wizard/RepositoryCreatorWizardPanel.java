package edu.stanford.smi.protegex.owl.ui.repository.wizard;

import edu.stanford.smi.protegex.owl.repository.Repository;

import javax.swing.*;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 26, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class RepositoryCreatorWizardPanel extends JComponent {

    public abstract Repository createRepository();
}

