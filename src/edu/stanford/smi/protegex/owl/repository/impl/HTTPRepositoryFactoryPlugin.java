package edu.stanford.smi.protegex.owl.repository.impl;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.factory.RepositoryFactoryPlugin;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 19, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class HTTPRepositoryFactoryPlugin implements RepositoryFactoryPlugin {

    public boolean isSuitable(OWLModel model, String s) {
        if (s.trim().startsWith("http:")) {
            try {
                // Check the URL is well formed
                new URL(s);
                return true;
            }
            catch (MalformedURLException e) {
                Log.getLogger().warning("[URL Repository Factory] Could not create a HTTP repository because the specified URL, " +
                        s + " was malformed.");
                return false;
            }
        }
        return false;
    }


    public Repository createRepository(OWLModel model, String s) {
        try {
            return new HTTPRepository(new URL(s));
        }
        catch (MalformedURLException e) {
            return null;
        }
    }
}

