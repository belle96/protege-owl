package edu.stanford.smi.protegex.owl.ui.repository.action;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.ui.repository.AbstractRepositoriesPanel;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 3, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface RepositoryAction {

    public String getName();


    public boolean isSuitable(Repository repository, OWLModel owlModel);


    public void actionPerformed(Repository repository, AbstractRepositoriesPanel repositoriesPanel, OWLModel model);
}
