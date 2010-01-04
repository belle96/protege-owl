package edu.stanford.smi.protegex.owl.ui.owltable;

import javax.swing.*;

/**
 * A simple base implementation of OWLTableAction for simple subclassing.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLTableAction extends AbstractAction
        implements OWLTableAction {

    public AbstractOWLTableAction(String name, Icon icon) {
        super(name, icon);
    }
}
