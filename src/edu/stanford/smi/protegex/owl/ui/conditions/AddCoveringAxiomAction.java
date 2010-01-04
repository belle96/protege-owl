package edu.stanford.smi.protegex.owl.ui.conditions;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.cls.AddSubclassAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourcedisplay.ResourceDisplay;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

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
        if (isSuitable(getComponent(), namedClass)) {
            performAction(namedClass);
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


    public boolean isSuitable(Component component,
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
