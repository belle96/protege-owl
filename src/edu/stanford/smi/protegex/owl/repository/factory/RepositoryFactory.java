package edu.stanford.smi.protegex.owl.repository.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import edu.stanford.smi.protege.plugin.PluginUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.impl.DatabaseRepositoryFactoryPlugin;
import edu.stanford.smi.protegex.owl.repository.impl.DublinCoreDLVersionRedirectRepositoryFactoryPlugin;
import edu.stanford.smi.protegex.owl.repository.impl.FTPRepositoryFactoryPlugin;
import edu.stanford.smi.protegex.owl.repository.impl.HTTPRepositoryFactoryPlugin;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFileRepositoryFactoryPlugin;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFolderRepositoryFactoryPlugin;
import edu.stanford.smi.protegex.owl.repository.impl.RelativeFileRepositoryFactoryPlugin;
import edu.stanford.smi.protegex.owl.repository.impl.RelativeFolderRepositoryFactoryPlugin;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 19, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RepositoryFactory {

    private static RepositoryFactory instance;

    private ArrayList<RepositoryFactoryPlugin> factories;


    @SuppressWarnings("unchecked")
    private RepositoryFactory() {
        factories = new ArrayList<RepositoryFactoryPlugin>();
        factories.add(new DublinCoreDLVersionRedirectRepositoryFactoryPlugin());
        factories.add(new LocalFileRepositoryFactoryPlugin());
        factories.add(new RelativeFileRepositoryFactoryPlugin());
        factories.add(new LocalFolderRepositoryFactoryPlugin());
        factories.add(new HTTPRepositoryFactoryPlugin());
        factories.add(new RelativeFolderRepositoryFactoryPlugin());
        factories.add(new FTPRepositoryFactoryPlugin());
        factories.add(new DatabaseRepositoryFactoryPlugin());
        Collection<Class> plugins = PluginUtilities.getClassesWithAttribute(RepositoryFactoryPlugin.PLUGIN_TYPE, "True");
        for (Iterator<Class> it = plugins.iterator(); it.hasNext();) {
            Class cls =  it.next();
            try {
                factories.add((RepositoryFactoryPlugin) cls.newInstance());
            }
            catch (InstantiationException e) {
              Log.getLogger().log(Level.SEVERE, "Exception caught", e);
            }
            catch (IllegalAccessException e) {
              Log.getLogger().log(Level.SEVERE, "Exception caught", e);
            }
        }
    }


    public static RepositoryFactory getInstance() {
        if (instance == null) {
            instance = new RepositoryFactory();
        }
        return instance;
    }


    public void registerRepositoryFactoryPlugin(RepositoryFactoryPlugin factory) {
        factories.add(factory);
    }


    public Repository createOntRepository(OWLModel model, String s) {
        for (Iterator<RepositoryFactoryPlugin> it = factories.iterator(); it.hasNext();) {
            RepositoryFactoryPlugin curPlugin = it.next();
            if (curPlugin.isSuitable(model, s)) {
                return curPlugin.createRepository(model, s);
            }
        }
        return null;
    }


    public Collection<RepositoryFactoryPlugin> getFactories() {
        return new ArrayList<RepositoryFactoryPlugin>(factories);
    }
}

