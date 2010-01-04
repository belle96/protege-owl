package edu.stanford.smi.protegex.owl.ui.menu.code;

import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.emf.EMFGenerator;
import edu.stanford.smi.protegex.owl.emf.EditableEMFGeneratorOptions;
import edu.stanford.smi.protegex.owl.emf.ProjectBasedEMFGeneratorOptions;
import edu.stanford.smi.protegex.owl.javacode.JavaCodeGeneratorAction;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class EMFAction extends AbstractOWLModelAction {

    public String getMenubarPath() {
        return CODE_MENU + PATH_SEPARATOR + JavaCodeGeneratorAction.GROUP;
    }


    public String getName() {
        return "Generate EMF Java Interfaces...";
    }


    public void run(OWLModel owlModel) {
        EditableEMFGeneratorOptions options = new ProjectBasedEMFGeneratorOptions(owlModel);
        EMFPanel panel = new EMFPanel(options);
        if (ProtegeUI.getModalDialogFactory().showDialog(ProtegeUI.getTopLevelContainer(owlModel.getProject()), panel,
                getName(), ModalDialogFactory.MODE_OK_CANCEL) == ModalDialogFactory.OPTION_OK) {
            panel.ok();
            EMFGenerator creator = new EMFGenerator(owlModel, options);
            try {
                creator.createAllInterfaces();
                ProtegeUI.getModalDialogFactory().showMessageDialog(owlModel, "EMF Interfaces generated.");
            }
            catch (Exception ex) {
                Log.getLogger().log(Level.SEVERE, "Exception caught", ex);
                ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel,
                        "Could not create EMF code:\n" + ex);
            }
        }
    }
}
