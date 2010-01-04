package edu.stanford.smi.protegex.owl.repository.impl;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.factory.RepositoryFactoryPlugin;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 3, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class FTPRepositoryFactoryPlugin implements RepositoryFactoryPlugin {

    public boolean isSuitable(OWLModel model,
                              String repositoryDescriptor) {
        if (model.getProject() != null) {
            if (model.getProject().getProjectURI() != null) {
                try {
                    URI uri = new URI(repositoryDescriptor);
                    return uri.getScheme().equals("ftp");
                }
                catch (URISyntaxException e) {
                    return false;
                }
            }
        }
        return false;
    }


    public Repository createRepository(OWLModel model,
                                       String repositoryDescriptor) {
        try {
            URI projectURI = model.getProject().getProjectURI();
            File f = new File(projectURI);
            URI projectDirectoryURI = f.getParentFile().toURI();
            URI ftpURI = new URI(repositoryDescriptor);
            FTPRepository repository = new FTPRepository(ftpURI, projectDirectoryURI);
            return repository;
        }
        catch (URISyntaxException e) {
            return null;
        }
    }
}

