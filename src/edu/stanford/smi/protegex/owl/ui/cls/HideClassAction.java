package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A ResourceAction to set the selected class to invisible.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class HideClassAction extends ResourceAction {

    public HideClassAction() {
        super("Hide class", Icons.getBlankIcon(), AddSubclassAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        getResource().setVisible(false);
        getComponent().repaint();
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof ClassTreePanel &&
                resource instanceof RDFSNamedClass &&
                resource.isVisible();
    }
}
