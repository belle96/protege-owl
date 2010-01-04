package edu.stanford.smi.protegex.owl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

import edu.stanford.smi.protege.Application;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.util.ApplicationProperties;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.MessageError;
import edu.stanford.smi.protege.util.MessageError.Severity;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.creator.NewOwlProjectCreator;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromReaderCreator;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromStreamCreator;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromUriCreator;
import edu.stanford.smi.protegex.owl.model.factory.FactoryUtils;

/**
 * A singleton that provides several generic services such as creating
 * new OWLModels.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProtegeOWL {

    /**
     * The name of the OWL Plugin folder (subfolder of the plugins directory)
     */
    public static String PLUGIN_FOLDER = "edu.stanford.smi.protegex.owl";
    
    private static File customPluginFolder = null;


    /**
     * Creates a new, empty JenaOWLModel.
     *
     * @return a new OWLModel
     */
    public static JenaOWLModel createJenaOWLModel() throws OntologyLoadException {
        Collection errors = new ArrayList();
        NewOwlProjectCreator creator = new NewOwlProjectCreator();
        creator.setOntologyName(FactoryUtils.generateOntologyURIBase());
        creator.create(errors);
        handleErrors(errors);
        return creator.getOwlModel();
	}
   

    @SuppressWarnings("unchecked")
    public static JenaOWLModel createJenaOWLModelFromInputStream(InputStream is) throws OntologyLoadException {
        Collection errors = new ArrayList();
        OwlProjectFromStreamCreator creator = new OwlProjectFromStreamCreator();
        creator.setStream(is);
        creator.create(errors);
        JenaOWLModel owlModel =  creator.getOwlModel();
        handleErrors(errors);
        return owlModel;
    }

    @SuppressWarnings("unchecked")
    public static JenaOWLModel createJenaOWLModelFromReader(Reader reader) throws OntologyLoadException {
        Collection errors = new ArrayList();
        OwlProjectFromReaderCreator creator = new OwlProjectFromReaderCreator();
        creator.setReader(reader);
        creator.create(errors);
        JenaOWLModel owlModel = creator.getOwlModel();
        handleErrors(errors);
        return owlModel;
    }

    @SuppressWarnings("unchecked")
    public static JenaOWLModel createJenaOWLModelFromURI(String uri) throws OntologyLoadException {
        Collection errors = new ArrayList();
        OwlProjectFromUriCreator creator = new OwlProjectFromUriCreator();
        creator.setOntologyUri(uri);
        creator.create(errors);
        JenaOWLModel owlModel = creator.getOwlModel();
        handleErrors(errors);
        return owlModel;
    }
    

    /**
     * Gets the plugin folder, which is the subfolder plugins/edu.stanford.smi.protegex.owl
     * of the application's starting directory.
     * @return the plugin folder
     */
    public static File getPluginFolder() {
        if (customPluginFolder == null) {
            return new File(new File(ApplicationProperties.getApplicationDirectory(), "plugins"),
                            PLUGIN_FOLDER);
        }
        else { 
            return customPluginFolder; 
        }
    }


    public static void setPluginFolder(File customPluginFolder) {
        ProtegeOWL.customPluginFolder = customPluginFolder;
    }
    /**
     * Initializes a Project so that it points to an existing OWL file.
     * This is typically used in conjunction with the <CODE>createJenaOWLModel()</CODE>
     * methods to create and save a new project.  The resulting project can then be loaded
     * into a Protege UI using the main method.
     *
     * @param project
     * @param owlFilePath
     * @param language
     */
    public static void initProject(Project project, String owlFilePath, String language) {
        JenaKnowledgeBaseFactory.setOWLFileLanguage(project.getSources(), language);
        JenaKnowledgeBaseFactory.setOWLFileName(project.getSources(), owlFilePath);
    }

    private static void handleErrors(Collection errors) throws OntologyLoadException {
        for (Object o : errors) {
            if (o instanceof Throwable) {
                Throwable t = (Throwable) o;
                Log.getLogger().log(Level.WARNING, "Exception caught ", (Throwable) o);
                throw t instanceof OntologyLoadException ? (OntologyLoadException) t : new OntologyLoadException(t, t.getMessage());
            }
            else if (o instanceof MessageError) {
                MessageError me = (MessageError) o;
                if (me.getSeverity() == Severity.FATAL && me.getException() != null) {
                    Throwable t = me.getException();
                    throw t instanceof OntologyLoadException ? (OntologyLoadException) t : new OntologyLoadException(t, t.getMessage());
                }
                else {
                    Log.getLogger().warning("Error found " + o);
                }
            }
            else {
                Log.getLogger().warning("Error found " + o);
            }
        }
    }

    /**
     * Starts the Protege UI, optionally with a given Project file.
     *
     * @param args the first String may be the name of a .pprj file
     */
    public static void main(String[] args) {
        Application.main(args);
    }
}
