package edu.stanford.smi.protegex.owl.repository.impl;

import edu.stanford.smi.protegex.owl.ProtegeOWL;

import java.net.URI;


/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 18, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ProtegeOWLPluginFolderRepository extends LocalFolderRepository {

    public ProtegeOWLPluginFolderRepository() {
        super(ProtegeOWL.getPluginFolder());
    }


    public String getRepositoryDescription() {
        return "Protege-OWL plugin folder";
    }


    public boolean isSystem() {
        return true;
    }


    public boolean isWritable(URI ontologyName) {
        return false;
    }
}

