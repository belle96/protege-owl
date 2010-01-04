package edu.stanford.smi.protegex.owl.ui.refactoring;

import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;

import javax.swing.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class RefactorResourceAction extends ResourceAction {

    public final static String GROUP = "Refactor/";


    public RefactorResourceAction(String name, Icon icon) {
        super(name, icon, GROUP);
    }
}
