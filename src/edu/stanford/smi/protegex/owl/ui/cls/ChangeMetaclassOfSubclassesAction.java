package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.WaitCursor;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ChangeMetaclassOfSubclassesAction extends ResourceAction {

    /**
     * @deprecated use ChangeMetaclassAction.GROUP
     */
    public static String GROUP = "Metaclasses";


    public ChangeMetaclassOfSubclassesAction() {
        super("Change metaclass of subclasses...", Icons.getBlankIcon(), ChangeMetaclassAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        Cls cls = (Cls) getResource();
        Cls metaCls = cls.getDirectType();
        String text = "Change metaclass of all subclasses of ";
        text += cls.getBrowserText();
        text += " to " + metaCls.getBrowserText();
        OWLModel owlModel = (OWLModel) cls.getKnowledgeBase();
        if (ProtegeUI.getModalDialogFactory().showConfirmDialog(getComponent(), text, "Confirm")) {
            WaitCursor waitCursor = new WaitCursor(getComponent());
            try {
                Iterator i = cls.getSubclasses().iterator();
                while (i.hasNext()) {
                    Cls subclass = (Cls) i.next();
                    if (subclass.isEditable() && subclass instanceof RDFSNamedClass) {
                        subclass.setDirectType(metaCls);
                    }
                }
            }
            finally {
                waitCursor.hide();
            }
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        if (component instanceof OWLSubclassPane && ((Cls) resource).getDirectSubclassCount() > 0) {
            Cls cls = (Cls) resource;
            Cls type = cls.getDirectType();
            for (Iterator it = cls.getDirectSubclasses().iterator(); it.hasNext();) {
                Cls subCls = (Cls) it.next();
                if (subCls.isVisible() && !type.equals(subCls.getDirectType())) {
                    return true;
                }
            }
        }
        return false;
    }
}
