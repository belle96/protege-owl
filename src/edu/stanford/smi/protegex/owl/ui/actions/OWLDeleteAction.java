package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protege.util.DeleteAction;
import edu.stanford.smi.protege.util.Selectable;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLDeleteAction extends DeleteAction {

    public OWLDeleteAction(String text, Selectable selectable) {
        super(text, selectable);
        putValue(Action.SMALL_ICON, OWLIcons.getDeleteIcon());
    }
}
