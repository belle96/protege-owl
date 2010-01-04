package edu.stanford.smi.protegex.owl.ui.classform.component.property;

import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * An Action to toggle the closure state of a PropertyFormTable.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ChangeClosureAxiomButton extends AbstractAction {

    private JButton button;

    private final static String CLOSED_TEXT = "Remove closure axiom (Allow other values)";

    private final static String OPEN_TEXT = "Add closure axiom (Don't allow other values)";

    private PropertyFormTable table;


    public ChangeClosureAxiomButton(PropertyFormTable table) {
        super(CLOSED_TEXT, OWLIcons.getImageIcon(OWLIcons.CLOSURE_CLOSED));
        this.table = table;
    }


    public void actionPerformed(ActionEvent e) {
        table.setClosed(!table.isClosed());
        updateButton();
    }


    public void init(JButton button) {
        this.button = button;
        updateButton();
    }


    private void updateButton() {
        boolean closed = table.isClosed();
        button.setToolTipText(closed ? CLOSED_TEXT : OPEN_TEXT);
        button.setIcon(OWLIcons.getImageIcon(closed ? OWLIcons.CLOSURE_CLOSED : OWLIcons.CLOSURE_OPEN));
    }
}
