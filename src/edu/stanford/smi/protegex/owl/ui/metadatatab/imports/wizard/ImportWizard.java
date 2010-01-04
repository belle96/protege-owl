package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import javax.swing.JComponent;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizard;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 28, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ImportWizard extends OWLWizard {

    private OWLModel owlModel;

	private ImportWizardData importData;


    public ImportWizard(JComponent component, OWLModel owlModel) {
        super(component, "Import Ontology");
        this.owlModel = owlModel;
	    importData = new ImportWizardData();
	    // First page is the import type page
        addPage(new ImportTypePage(this));
    }


	public ImportWizardData getImportData() {
		return importData;
	}


    public OWLModel getOWLModel() {
        return owlModel;
    }

	public void reset() {
		importData = new ImportWizardData();
	}

	public static void main(String  [] args) throws OntologyLoadException {
		OWLModel owlModel = ProtegeOWL.createJenaOWLModel();
		ImportWizard wizard = new ImportWizard(null, owlModel);
		wizard.show();
	}
}

