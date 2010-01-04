package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFSSubClassOfWidgetMetadata implements OWLWidgetMetadata {

    public int getSuitability(RDFSNamedClass cls, RDFProperty property) {
        if (property.equals(property.getOWLModel().getRDFSSubClassOfProperty())) {
            return DEFAULT;
        }
        else {
            return NOT_SUITABLE;
        }
    }
}
