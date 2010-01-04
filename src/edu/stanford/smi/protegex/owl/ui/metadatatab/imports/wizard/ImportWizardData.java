package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

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
public class ImportWizardData {

	private HashSet importEntries;

	public ImportWizardData() {
		importEntries = new HashSet();
	}


	public HashSet getImportEntries() {
		return importEntries;
	}

	public void addImportEntry(ImportEntry importEntry) {
		importEntries.add(importEntry);
	}

}

