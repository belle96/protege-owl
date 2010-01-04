package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import edu.stanford.smi.protegex.owl.repository.Repository;

import java.net.URI;
import java.util.Collection;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Dec 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface ImportEntry {

	public boolean isPossibleToImport();

	public URI getOntologyURI();

	public Repository getRepository();

	public Repository getRepositoryToAdd();

	public Collection getErrors();
}

