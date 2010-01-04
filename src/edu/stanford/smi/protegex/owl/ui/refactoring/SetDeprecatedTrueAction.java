package edu.stanford.smi.protegex.owl.ui.refactoring;

import edu.stanford.smi.protegex.owl.model.Deprecatable;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A ResourceAction to set a class or property to deprecated.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SetDeprecatedTrueAction extends RefactorResourceAction {

    public SetDeprecatedTrueAction() {
        super("Set deprecation flag", OWLIcons.getAddIcon("Deprecated"));
    }


    public void actionPerformed(ActionEvent e) {
        try {
            getOWLModel().beginTransaction("" + getValue(Action.NAME) + " from " + getResource().getBrowserText(), getResource().getName());
            ((Deprecatable) getResource()).setDeprecated(true);
            getOWLModel().commitTransaction();
        }
        catch (Exception ex) {
        	getOWLModel().rollbackTransaction();
            OWLUI.handleError(getOWLModel(), ex);
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return //frame.isEditable() &&
                resource instanceof Deprecatable &&
                        !((Deprecatable) resource).isDeprecated();
    }
}
