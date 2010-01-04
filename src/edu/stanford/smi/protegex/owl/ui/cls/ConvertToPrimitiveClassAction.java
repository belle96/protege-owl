package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourcedisplay.ResourceDisplay;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A ResourceAction to convert a primitive class into a defined class.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConvertToPrimitiveClassAction extends ResourceAction {

    public ConvertToPrimitiveClassAction() {
        super("Convert to primitive class",
                OWLIcons.getImageIcon(OWLIcons.PRIMITIVE_OWL_CLASS),
                AddSubclassAction.GROUP,
                true);
    }


    public void actionPerformed(ActionEvent e) {
        OWLClassesTab tab = OWLClassesTab.getOWLClassesTab(getComponent());
       
        try {
            final OWLNamedClass cls = (OWLNamedClass) getResource();
            getOWLModel().beginTransaction("Convert " + cls.getBrowserText() + " to primitive class", (cls == null ? null : cls.getName()));
            performAction(cls);
            getOWLModel().commitTransaction();
        }
        catch (Exception ex) {
        	getOWLModel().rollbackTransaction();
            OWLUI.handleError(getOWLModel(), ex);
        }

        if (tab != null) {
            tab.ensureClassSelected((OWLNamedClass) getResource(), -1);
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof ResourceDisplay &&
                resource instanceof OWLNamedClass &&
                resource.isEditable() &&
                ((OWLNamedClass) resource).getDefinition() != null;
    }


    public static void performAction(OWLNamedClass cls) {
        Collection equis = new ArrayList(cls.getEquivalentClasses());
        for (Iterator it = equis.iterator(); it.hasNext();) {
            RDFSClass equiClass = (RDFSClass) it.next();
            if (equiClass instanceof OWLIntersectionClass) {
                OWLIntersectionClass intersectionClass = (OWLIntersectionClass) equiClass;
                Collection operands = intersectionClass.getOperands();
                for (Iterator ot = operands.iterator(); ot.hasNext();) {
                    RDFSClass operand = (RDFSClass) ot.next();
                    cls.addSuperclass(operand.createClone());
                }
                intersectionClass.delete();
                for (Iterator oit = operands.iterator(); oit.hasNext();) {
                    RDFSClass operand = (RDFSClass) oit.next();
                    if (operand instanceof RDFSNamedClass) {
                        cls.addSuperclass(operand);
                    }
                }
            }
            else if (equiClass != null) {
                equiClass.removeSuperclass(cls);
                if (equiClass instanceof RDFSNamedClass && equiClass.getSuperclassCount() == 0) {
                    equiClass.addSuperclass(equiClass.getOWLModel().getOWLThingClass());
                }
            }
        }
    }
}
