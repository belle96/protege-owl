package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddSubclassAction extends ResourceAction {

    public final static String GROUP = "Edit";


    public AddSubclassAction() {
        super("Add subclass...",
                OWLIcons.getAddIcon(OWLIcons.PRIMITIVE_OWL_CLASS), GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        RDFSClass cls = ProtegeUI.getSelectionDialogFactory().selectClass(getComponent(), getOWLModel());
        if (cls != null && cls.isEditable()) {
            RDFSNamedClass superClass = (RDFSNamedClass) getResource();
            if (!superClass.equals(cls)) {
                if (!cls.getSuperclasses(true).contains(superClass)) {
                    cls.addSuperclass(superClass);
                }
                else {
                    ProtegeUI.getModalDialogFactory().showMessageDialog(getComponent(),
                            cls.getBrowserText() + " is already a subclass of " + cls.getBrowserText() + ".");
                }
            }
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return false; // component instanceof OWLSubclassPane && resource instanceof RDFSNamedClass;
    }
}
