package edu.stanford.smi.protegex.owl.ui.repository;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.RepositoryManager;

import java.util.List;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 27, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class GlobalRepositoriesPanel extends AbstractRepositoriesPanel {

    public GlobalRepositoriesPanel(OWLModel model, RepositoryManager man) {
        super(model, man);
    }


    public List getRepositories() {
        return getRepositoryManager().getGlobalRepositories();
    }


    public void addRepository(Repository repository) {
        getRepositoryManager().addGlobalRepository(0, repository);
    }


    public String getRepositoriesTitle() {
        return "Global repositories";
    }
}

