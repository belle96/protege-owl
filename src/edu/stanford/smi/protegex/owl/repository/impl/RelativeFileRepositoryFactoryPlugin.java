package edu.stanford.smi.protegex.owl.repository.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.factory.RepositoryFactoryPlugin;
import edu.stanford.smi.protegex.owl.repository.util.RepositoryUtil;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 22, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RelativeFileRepositoryFactoryPlugin implements RepositoryFactoryPlugin {


    public boolean isSuitable(OWLModel model, String s) {
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
						File file = RepositoryUtil.getRepositoryFileFromRelativePath(model, RepositoryUtil.stripQuery(s));
						return (file != null);
					} else {
						return false;
					}
				} catch (URISyntaxException e) {
					return false;
				}
			}
		}
		return false;
	}
  
    
    public Repository createRepository(OWLModel model, String s) {
		URI owlFileUri = model.getProject().getProjectURI();

		if (owlFileUri == null
				&& model.getKnowledgeBaseFactory() instanceof JenaKnowledgeBaseFactory) {
			String uriString = JenaKnowledgeBaseFactory.getOWLFilePath(model.getProject().getSources());
			owlFileUri = URIUtilities.createURI(uriString);
		}

		if (owlFileUri == null) {
			return null;
		}
		File file = RepositoryUtil.getRepositoryFileFromRelativePath(model, s);			
		if (file != null) {			
			try {
				return new RelativeFileRepository(file, owlFileUri.toURL(), s.trim());
			} catch (MalformedURLException e) {
				return null;
			} catch (URISyntaxException e) {
				return null;			
			}
		}
		return null;
	}
    
    
 
}

