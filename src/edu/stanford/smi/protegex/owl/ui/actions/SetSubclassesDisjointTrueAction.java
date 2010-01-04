package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.refactoring.RefactorResourceAction;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SetSubclassesDisjointTrueAction extends RefactorResourceAction {

    public SetSubclassesDisjointTrueAction() {
        super("Set all subclasses disjoint",
                OWLIcons.getImageIcon("PrimitiveClassSD"));
    }


    public void actionPerformed(ActionEvent e) {
        OWLModel owlModel = getResource().getOWLModel();
        RDFProperty property = owlModel.getProtegeSubclassesDisjointProperty();
        if (property == null) {
            ProtegeUI.getModalDialogFactory().showMessageDialog(owlModel,
                    "Before you can use this feature, you need to import\n" +
                            "the Protege metadata ontology.  Activate this using\n" +
                            "OWL/Preferences.../Import Protege metadata ontology.");
        }
        else {
            try {
                OWLNamedClass cls = (OWLNamedClass) getResource();
                owlModel.beginTransaction("" + getValue(Action.NAME) + " at " + cls.getBrowserText(), (cls == null ? null : cls.getName()));
                cls.setSubclassesDisjoint(true);
                for (Iterator it = cls.getSubclasses(true).iterator(); it.hasNext();) {
                    RDFSClass subCls = (RDFSClass) it.next();
                    if (subCls instanceof OWLNamedClass && subCls.isEditable()) {
                        ((OWLNamedClass) subCls).setSubclassesDisjoint(true);
                    }
                }
                owlModel.commitTransaction();
            }
            catch (Exception ex) {
            	owlModel.rollbackTransaction();            	
                OWLUI.handleError(owlModel, ex);
            }
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource.isEditable() &&
                resource instanceof OWLNamedClass &&
                !((OWLNamedClass) resource).getSubclassesDisjoint();
    }
}
