package edu.stanford.smi.protegex.owl.ui.actions.triple;

import edu.stanford.smi.protegex.owl.model.triplestore.Triple;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * A Swing Action that wraps a (generic) TripleAction.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TripleActionSwingAction extends AbstractAction {

    private TripleAction tripleAction;

    private Triple triple;


    public TripleActionSwingAction(TripleAction tripleAction, Triple triple) {
        super(tripleAction.getName(), getIcon(tripleAction));
        this.triple = triple;
        this.tripleAction = tripleAction;
    }


    public void actionPerformed(ActionEvent e) {
        tripleAction.run(triple);
    }


    public static Icon getIcon(TripleAction action) {
        String fileName = action.getIconFileName();
        if (fileName == null) {
            return null;
        }
        else {
            Class c = action.getIconResourceClass();
            if (c == null) {
                c = OWLIcons.class;
            }
            if (!fileName.endsWith(".gif") && !fileName.endsWith(".png")) {
                fileName += ".gif";
            }
            return OWLIcons.getImageIcon(fileName, c);
        }
    }
}
