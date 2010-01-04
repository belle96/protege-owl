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
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConvertToDefinedClassAction extends ResourceAction {

    public ConvertToDefinedClassAction() {
        super("Convert to defined class",
                OWLIcons.getImageIcon(OWLIcons.DEFINED_OWL_CLASS),
                AddSubclassAction.GROUP, true);
    }


    public void actionPerformed(ActionEvent e) {
        try {
            final OWLNamedClass cls = (OWLNamedClass) getResource();
            getOWLModel().beginTransaction("Convert " + cls.getBrowserText() + " to defined class", (cls == null ? null : cls.getName()));
            performAction(cls);
            getOWLModel().commitTransaction();
        }
        catch (Exception ex) {
        	getOWLModel().rollbackTransaction();
            OWLUI.handleError(getOWLModel(), ex);
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof ResourceDisplay &&
                resource instanceof OWLNamedClass &&
                resource.isEditable() &&
                ((OWLNamedClass) resource).getDefinition() == null;
    }


    public static void performAction(OWLNamedClass namedClass) {
        OWLModel owlModel = namedClass.getOWLModel();
        Collection superclasses = new ArrayList(namedClass.getSuperclasses(false));
        superclasses.remove(namedClass.getOWLModel().getOWLThingClass());
        if (superclasses.size() == 1) {
            RDFSClass superclass = (RDFSClass) superclasses.iterator().next();
            superclass.addSuperclass(namedClass);
        }
        else if (superclasses.size() > 1) {
            OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
            for (Iterator it = superclasses.iterator(); it.hasNext();) {
                RDFSClass superClass = (RDFSClass) it.next();
                intersectionCls.addOperand(superClass.createClone());
            }
            namedClass.setDefinition(intersectionCls);
            for (Iterator it = superclasses.iterator(); it.hasNext();) {
                RDFSClass superClass = (RDFSClass) it.next();
                if (superClass instanceof OWLAnonymousClass) {
                    superClass.delete();
                }
            }
        }
    }
}
