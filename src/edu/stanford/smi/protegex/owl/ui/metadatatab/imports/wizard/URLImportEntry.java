package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.impl.ForcedURLRetrievalRepository;
import edu.stanford.smi.protegex.owl.repository.impl.HTTPRepository;
import edu.stanford.smi.protegex.owl.repository.util.OntologyNameExtractor;
import edu.stanford.smi.protegex.owl.repository.util.URLInputSource;

import java.net.URI;
import java.net.URL;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Dec 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class URLImportEntry extends AbstractImportEntry {

	private URL url;

	public URLImportEntry(URL url) {
		this.url = url;
	}


	public boolean isPossibleToImport() {
		try {
			OntologyNameExtractor extractor = new OntologyNameExtractor(new URLInputSource(url));
			URI uri = extractor.getOntologyName();
			if(uri != null) {
				setRepository(new HTTPRepository(url));
			}
			else {
				if(extractor.isPossiblyValidOntology()) {
					uri = new URI(url.toString());
					setRepository(new ForcedURLRetrievalRepository(url));
				} else {
					throw new IllegalArgumentException("The document pointed to by " + url + " does not " +
					                                   "appear to be a valid ontology.");
				}
			}
			setOntologyURI(uri);
			return true;
		}
		catch(Exception e) {
			addError(e);
			setOntologyURI(null);
			setRepository(null);
			return false;
		}
	}


	public Repository getRepositoryToAdd() {
		return getRepository();
	}
}

