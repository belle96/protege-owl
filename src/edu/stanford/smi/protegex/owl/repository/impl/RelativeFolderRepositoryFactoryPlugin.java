package edu.stanford.smi.protegex.owl.repository.impl;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.factory.RepositoryFactoryPlugin;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 26, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RelativeFolderRepositoryFactoryPlugin implements RepositoryFactoryPlugin {

    public boolean isSuitable(OWLModel model,
                              String s) {
        if (model.getProject() != null) {
        
           URI owlFileUri = model.getProject().getProjectURI();
        
           if (owlFileUri == null && model.getKnowledgeBaseFactory() instanceof JenaKnowledgeBaseFactory) {
        	   String uriString = JenaKnowledgeBaseFactory.getOWLFilePath(model.getProject().getSources());
        	   owlFileUri = URIUtilities.createURI(uriString);
           } 
           
        	if (owlFileUri != null) {
                try {
                    URI uri = new URI(s.trim());
                    if (uri.isAbsolute() == false) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                catch (URISyntaxException e) {
                    return false;
                }
            }
        }
        return false;
    }


    public Repository createRepository(OWLModel model,
                                       String s) {
        try {
        	
            URI owlFileUri = model.getProject().getProjectURI();
            
            if (owlFileUri == null && model.getKnowledgeBaseFactory() instanceof JenaKnowledgeBaseFactory) {
         	   String uriString = JenaKnowledgeBaseFactory.getOWLFilePath(model.getProject().getSources());
         	   owlFileUri = URIUtilities.createURI(uriString);
            } 
        	
            if (owlFileUri == null) {
            	return null;
            }
            
            return new RelativeFolderRepository(owlFileUri.toURL(), s.trim());
        }
        catch (MalformedURLException e) {
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
        catch (URISyntaxException e) {
          Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
        return null;
    }
}

