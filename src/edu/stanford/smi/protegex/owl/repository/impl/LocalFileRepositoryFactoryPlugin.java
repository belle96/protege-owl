package edu.stanford.smi.protegex.owl.repository.impl;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.factory.RepositoryFactoryPlugin;
import edu.stanford.smi.protegex.owl.repository.util.RepositoryUtil;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 22, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class LocalFileRepositoryFactoryPlugin implements RepositoryFactoryPlugin {
    public final static String FILE_PREFIX = "file:";
    
    public boolean isSuitable(OWLModel model, String s) {
        return getRepositoryFile(model, s) != null;
    }


    public Repository createRepository(OWLModel model, String s) {
        try {
            URI u = new URI(s);
            return new LocalFileRepository(getRepositoryFile(model, s), 
                                           RepositoryUtil.isForcedToBeReadOnly(u.getQuery()));
        }
        catch (URISyntaxException e) {
            return null;
        }
    }
    
    private File getRepositoryFile(OWLModel model, String s) {
        if (s.trim().startsWith(FILE_PREFIX)) {
            try {
                File f = new File(new URI(s).getPath());
                if (f.isFile() && f.canRead()) {
                    return f;
                }
            }
            catch (Throwable t) {
                return null;
            }
        }
        return null;
    }
}

