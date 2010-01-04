package edu.stanford.smi.protegex.owl.ui.repository;

import edu.stanford.smi.protege.model.Project;
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
public class ProjectRepositoriesPanel extends AbstractRepositoriesPanel {

    public ProjectRepositoriesPanel(OWLModel model, RepositoryManager man) {
        super(model, man);
        Project project = model.getProject();
        if (project != null) {
            setAddRepositoryEnabled(true);
        }
    }


    public List getRepositories() {
        return getRepositoryManager().getProjectRepositories();
    }


    public void addRepository(Repository repository) {
        getRepositoryManager().addProjectRepository(0, repository);
    }


    public String getRepositoriesTitle() {
        return "Project repositories";
    }
}

