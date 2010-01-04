package edu.stanford.smi.protegex.owl.javacode;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.logging.Level;

import javax.swing.Action;
import javax.swing.JTree;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.cls.ExtractTaxonomyAction;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JavaCodeGeneratorResourceAction extends ResourceAction {

    public JavaCodeGeneratorResourceAction() {
        super("Generate Protege-OWL Java Code for class...", Icons.getBlankIcon(),
                ExtractTaxonomyAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        OWLModel owlModel = getOWLModel();
        EditableJavaCodeGeneratorOptions options = new ProjectBasedJavaCodeGeneratorOptions(owlModel);
        JavaCodeGeneratorPanel panel = new JavaCodeGeneratorPanel(options);
        if (ProtegeUI.getModalDialogFactory().showDialog(ProtegeUI.getTopLevelContainer(owlModel.getProject()), panel,
                (String) getValue(Action.NAME), ModalDialogFactory.MODE_OK_CANCEL) == ModalDialogFactory.OPTION_OK) {
            panel.ok();
            JavaCodeGenerator creator = new JavaCodeGenerator(owlModel, options);
            try {
                RDFSNamedClass cls = (RDFSNamedClass) getResource();
                creator.createInterface(cls);
                creator.createImplementation(cls);
                creator.createFactoryClass();
                ProtegeUI.getModalDialogFactory().showMessageDialog(owlModel,
                        "Java code successfully generated for " + cls.getLocalName() + ".");
            }
            catch (Exception ex) {
                Log.getLogger().log(Level.SEVERE, "Exception caught", ex);
                ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel,
                        "Could not create Java code:\n" + ex);
            }
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource instanceof RDFSNamedClass &&
                !resource.isSystem() &&
                component instanceof JTree;
    }
}
