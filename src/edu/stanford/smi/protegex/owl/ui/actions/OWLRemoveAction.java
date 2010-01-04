package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protege.util.RemoveAction;
import edu.stanford.smi.protege.util.Selectable;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLRemoveAction extends RemoveAction {

    public OWLRemoveAction(String text, Selectable selectable) {
        this(text, OWLIcons.getRemoveIcon(), selectable);
    }


    public OWLRemoveAction(String text, Icon icon, Selectable selectable) {
        super(text, selectable);
        putValue(Action.SMALL_ICON, icon);
    }
}
