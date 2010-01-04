package edu.stanford.smi.protegex.owl.ui.resourceselection;

import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * The interface used by the ResourceSelectionComboBox to specify the action that is to be
 * performed when the user has selected a resource.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ResourceSelectionListener {

    void resourceSelected(RDFResource resource);
}
