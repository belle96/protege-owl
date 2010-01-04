package edu.stanford.smi.protegex.owl.ui.owltable;

import edu.stanford.smi.protegex.owl.model.RDFSClass;

import javax.swing.*;

/**
 * An AbstractAction that's enabling state depends on the chosen row in the table.
 * This is the base class for Actions like delete and toggle.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLTableAction extends Action {

    boolean isEnabledFor(RDFSClass cls, int rowIndex);
}
