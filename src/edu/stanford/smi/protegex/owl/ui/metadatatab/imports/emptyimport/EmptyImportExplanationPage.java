package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.emptyimport;

import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizard;
import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizardPage;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 5, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class EmptyImportExplanationPage extends OWLWizardPage {

    private OWLModel owlModel;


    public EmptyImportExplanationPage(OWLWizard wizard, OWLModel owlModel) {
        super("Explanation page", wizard);
        this.owlModel = owlModel;
        createUI();
    }


    private void createUI() {
	    setHelpText("Creating and importing an empty ontology.", HELP_TEXT);
    }


    public WizardPage getNextPage() {
        return new EmptyOntologyURIPage(getOWLWizard(), owlModel);
    }


    private static final String HELP_TEXT = "<p>This wizard will create an empty ontology and then " +
            "import it into this ontology.</p>" +
            "<p>The empty ontology will be " +
            "saved in a local file which can be later uploaded to a location " +
            "on the web that corresponds to the ontology URI that is specified " +
            "on the next page.</p>" +
            "<p>Any classes, properties or individuals that " +
            "are created in the new empty ontology will be available to this " +
            "ontology.</p>";


}

