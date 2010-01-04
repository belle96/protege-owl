package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import java.io.File;

import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFileRepository;
import edu.stanford.smi.protegex.owl.repository.util.FileInputSource;
import edu.stanford.smi.protegex.owl.repository.util.OntologyNameExtractor;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Dec 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class FileImportEntry extends AbstractImportEntry {

	private File file;

	public FileImportEntry(File file) {
		this.file = file;
	}


	public boolean isPossibleToImport() {
		try {
			OntologyNameExtractor nameExtractor = new OntologyNameExtractor(new FileInputSource(file));
			if(nameExtractor.isPossiblyValidOntology() == false) {
				throw new IllegalArgumentException("The document pointed to by " + file + " does not " +
				                                   "appear to contain a valid RDF/XML representation of an ontology.");
			}
			setOntologyURI(nameExtractor.getOntologyName());
			setRepository(new LocalFileRepository(file));
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

