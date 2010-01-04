package edu.stanford.smi.protegex.owl.ui.metadatatab.imports;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.ui.triplestore.CreateTripleStorePanel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateImportedTripleStorePanel extends CreateTripleStorePanel {

    public static final String TITLE = "Create empty imported ontology...";


    public CreateImportedTripleStorePanel(OWLModel owlModel) {
        super(owlModel);
    }


    public static void showDialog(OWLModel owlModel) {
        CreateImportedTripleStorePanel panel = new CreateImportedTripleStorePanel(owlModel);
        if (ProtegeUI.getModalDialogFactory().showDialog(ProtegeUI.getTopLevelContainer(owlModel.getProject()), panel,
                TITLE, ModalDialogFactory.MODE_OK_CANCEL) ==
                ModalDialogFactory.OPTION_OK) {
            panel.performAction();
        }
    }
}
