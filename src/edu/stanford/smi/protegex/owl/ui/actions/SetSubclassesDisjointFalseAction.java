package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
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
public class SetSubclassesDisjointFalseAction extends RefactorResourceAction {

    public SetSubclassesDisjointFalseAction() {
        super("Unset all subclasses disjoint",
                OWLIcons.getImageIcon(OWLIcons.PRIMITIVE_OWL_CLASS));
    }


    public void actionPerformed(ActionEvent e) {
        OWLNamedClass cls = (OWLNamedClass) getResource();
        OWLModel owlModel = cls.getOWLModel();
        try {
            owlModel.beginTransaction("" + getValue(Action.NAME) +
                    " at " + cls.getBrowserText(), cls.getName());
            cls.setSubclassesDisjoint(false);
            for (Iterator it = cls.getSubclasses(true).iterator(); it.hasNext();) {
                Cls subCls = (Cls) it.next();
                if (subCls instanceof OWLNamedClass && subCls.isEditable()) {
                    ((OWLNamedClass) subCls).setSubclassesDisjoint(false);
                }
            }
            owlModel.commitTransaction();
        }
        catch (Exception ex) {
        	owlModel.rollbackTransaction();
            OWLUI.handleError(owlModel, ex);
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource.isEditable() &&
                resource instanceof OWLNamedClass &&
                ((OWLNamedClass) resource).getSubclassesDisjoint();
    }
}
