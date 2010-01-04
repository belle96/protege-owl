package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import edu.stanford.smi.protegex.owl.repository.Repository;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Dec 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class AbstractImportEntry implements ImportEntry {

	private Collection errors;

	private URI ontologyURI;

	private Repository repository;

	public AbstractImportEntry() {
		errors = new HashSet();
	}

	protected void clearErrors() {
		errors.clear();
	}

	protected void addError(Object error) {
		errors.add(error);
	}

	public Collection getErrors() {
		return Collections.unmodifiableCollection(errors);
	}


	public URI getOntologyURI() {
		return ontologyURI;
	}


	protected void setOntologyURI(URI ontologyURI) {
		this.ontologyURI = ontologyURI;
	}


	public Repository getRepository() {
		return repository;
	}


	protected void setRepository(Repository repository) {
		this.repository = repository;
	}
}

