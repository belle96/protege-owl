package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import edu.stanford.smi.protegex.owl.repository.Repository;

import java.net.URI;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Dec 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RepositoryImportEntry extends AbstractImportEntry {

	private URI ontologyURI;

	private Repository repository;

	public RepositoryImportEntry(URI ontologyURI, Repository repository) {
		this.ontologyURI = ontologyURI;
		this.repository = repository;
	}


	public boolean isPossibleToImport() {
		setOntologyURI(ontologyURI);
		setRepository(repository);
		return true;
	}


	public Repository getRepositoryToAdd() {
		return null;
	}
}

