package edu.stanford.smi.protegex.owl.repository.impl;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.factory.RepositoryFactoryPlugin;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 11, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DublinCoreDLVersionRedirectRepositoryFactoryPlugin implements RepositoryFactoryPlugin {

    public boolean isSuitable(OWLModel model,
                              String repositoryDescriptor) {
        return repositoryDescriptor.trim().equals(DublinCoreDLVersionRedirectRepository.DESCRIPTOR);
    }


    public Repository createRepository(OWLModel model,
                                       String repositoryDescriptor) {
        return new DublinCoreDLVersionRedirectRepository();
    }
}

