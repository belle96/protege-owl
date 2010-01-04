package edu.stanford.smi.protegex.owl.repository.factory;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 19, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface RepositoryFactoryPlugin {

    public static final String PLUGIN_TYPE = "RepositoryFactoryPlugin";


    /**
     * Determines if this plugin can create the appropriate
     * repository from the specified <code>String</code>.
     *
     * @param repositoryDescriptor The repository descriptor (This is typically a URI)
     * @return <code>true</code> if the plugin can create an
     *         appropriate repository given the specified repository descriptor,
     *         or <code>false</code> if the plugin cannot create the appropriate
     *         repository.
     */
    public boolean isSuitable(OWLModel model, String repositoryDescriptor);


    /**
     * Creates a repository from the specified repository descriptor.
     *
     * @param model
     * @param repositoryDescriptor The repository descriptor.
     * @return A <code>Repository</code> that is based on the specified
     *         repository descriptor.
     */
    public Repository createRepository(OWLModel model, String repositoryDescriptor);
}
