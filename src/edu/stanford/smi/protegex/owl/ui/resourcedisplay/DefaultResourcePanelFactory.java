package edu.stanford.smi.protegex.owl.ui.resourcedisplay;

import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultResourcePanelFactory implements ResourcePanelFactory{

    public ResourcePanel createResourcePanel(OWLModel owlModel, int defaultType) {
        return new ResourceDisplay(owlModel.getProject(), defaultType);
    }
}
