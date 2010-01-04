package edu.stanford.smi.protegex.owl.ui.resourcedisplay;

import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * A factory of ResourcePanels, used by the ProtegeUI class.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ResourcePanelFactory {

    /**
     * Creates a new ResourcePanel.
     * @param defaultType  one of ResourcePanel.DEFAULT_TYPE_xxx
     * @return a new ResourcePanel
     */
    ResourcePanel createResourcePanel(OWLModel owlModel, int defaultType);
}
