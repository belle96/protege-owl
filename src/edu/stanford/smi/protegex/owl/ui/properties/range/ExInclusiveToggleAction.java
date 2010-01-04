package edu.stanford.smi.protegex.owl.ui.properties.range;

import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExInclusiveToggleAction extends AbstractAction {

    private Callback callback;

    private Icon exclusiveIcon;

    private Icon inclusiveIcon;


    public ExInclusiveToggleAction(String text, String exclusiveIconName, String inclusiveIconName, Callback callback) {
        super(text, OWLIcons.getImageIcon(inclusiveIconName));
        this.callback = callback;
        exclusiveIcon = OWLIcons.getImageIcon(exclusiveIconName);
        inclusiveIcon = OWLIcons.getImageIcon(inclusiveIconName);
    }


    public void actionPerformed(ActionEvent e) {
        setExclusive(!isExclusive());
        callback.assignInterval();
    }


    public boolean isExclusive() {
        return getValue(Action.SMALL_ICON).equals(exclusiveIcon);
    }


    public void setExclusive(boolean exclusive) {
        if (exclusive) {
            putValue(Action.SMALL_ICON, exclusiveIcon);
        }
        else {
            putValue(Action.SMALL_ICON, inclusiveIcon);
        }
    }


    public static interface Callback {

        void assignInterval();
    }
}
