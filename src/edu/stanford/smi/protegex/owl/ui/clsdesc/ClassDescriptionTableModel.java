package edu.stanford.smi.protegex.owl.ui.clsdesc;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.ui.owltable.OWLTableModel;

/**
 * A OWLTableModel with additional support for remove buttons.
 * This is the base interface of all TableModels in this package.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
interface ClassDescriptionTableModel extends OWLTableModel {


    boolean isRemoveEnabledFor(Cls cls);
}
