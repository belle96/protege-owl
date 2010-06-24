package edu.stanford.smi.protegex.owl.ui.conditions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.stanford.smi.protege.util.ModalDialog;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.cls.AddSubclassAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourcedisplay.ResourceDisplay;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * An Action to add a covering axiom.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddCoveringAxiomAction extends ResourceAction {

    public AddCoveringAxiomAction() {
        super("Add covering axiom",
                OWLIcons.getImageIcon(OWLIcons.OWL_UNION_CLASS),
                AddSubclassAction.GROUP,
                true);
    }


    public void actionPerformed(ActionEvent e) {
        OWLNamedClass namedClass = (OWLNamedClass) getResource();
        if (isSuitableActionForResource(getComponent(), namedClass)) {
            performAction(namedClass);
        } else {
            ModalDialog.showMessageDialog(getComponent(), namedClass == null ?
                    "Nothing selected" : "Add covering axiom action is not suitable for " +
                    namedClass.getBrowserText() + ".", "Not suitable");
        }
    }


    private Set getOperandsStringSet(Iterator os) {
        Set existingOperands = new HashSet();
        while (os.hasNext()) {
            RDFSClass operand = (RDFSClass) os.next();
            existingOperands.add(operand.getBrowserText());
        }
        return existingOperands;
    }


    private static java.util.List getPureSubclasses(OWLNamedClass namedClass) {
        Collection subclasses = namedClass.getSubclasses(false);
        java.util.List pureSubclasses = new ArrayList(subclasses);
        pureSubclasses.removeAll(namedClass.getEquivalentClasses());
        return pureSubclasses;
    }


    /*
     * Does not do the real suitability check because it was very expensive.
     * The check is now done only if the user performs the action.
     */
    @Override
    public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof ResourceDisplay &&
                resource instanceof OWLNamedClass &&
                !resource.isSystem();
    }

    private boolean isSuitableActionForResource(Component component,
                              RDFResource resource) {
        if (component instanceof ResourceDisplay &&
                resource instanceof OWLNamedClass &&
                !resource.isSystem()) {
            OWLNamedClass namedClass = (OWLNamedClass) resource;
            Collection pures = getPureSubclasses(namedClass);
            if (pures.size() > 1) {
                Set newOperands = getOperandsStringSet(pures.iterator());
                for (Iterator it = namedClass.getEquivalentClasses().iterator(); it.hasNext();) {
                    RDFSClass equi = (RDFSClass) it.next();
                    if (equi instanceof OWLUnionClass) {
                        Iterator os = ((OWLUnionClass) equi).getOperands().iterator();
                        Set existingOperands = getOperandsStringSet(os);
                        if (newOperands.equals(existingOperands)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }


    public static void performAction(OWLNamedClass namedClass) {
        Collection pures = getPureSubclasses(namedClass);
        if (pures.size() > 1) {
            OWLModel owlModel = namedClass.getOWLModel();
            try {
                owlModel.beginTransaction("Add covering axiom for " + namedClass.getBrowserText(), namedClass.getName());
                OWLUnionClass unionClass = namedClass.getOWLModel().createOWLUnionClass();
                for (Iterator it = pures.iterator(); it.hasNext();) {
                    RDFSClass subclass = (RDFSClass) it.next();
                    RDFSClass c = subclass.createClone();
                    unionClass.addOperand(c);
                }
                namedClass.addEquivalentClass(unionClass);
                owlModel.commitTransaction();
            }
            catch (Exception ex) {
            	owlModel.rollbackTransaction();
                OWLUI.handleError(owlModel, ex);
            }
        }
        else {
            ProtegeUI.getModalDialogFactory().showErrorMessageDialog(namedClass.getOWLModel(),
                    "The class " + namedClass.getBrowserText() + " has less than 2 pure subclasses.");
        }
    }
}
