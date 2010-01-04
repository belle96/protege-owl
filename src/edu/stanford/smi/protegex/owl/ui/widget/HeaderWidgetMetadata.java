package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class HeaderWidgetMetadata implements OWLWidgetMetadata {

    public int getSuitability(RDFSNamedClass cls, RDFProperty property) {
    	return SUITABLE;
    	/*
        if (property.getName().equals(ProtegeNames.Slot.INFERRED_TYPE)) {  // Any unused, visible property
            return SUITABLE;
        }
        else {
            return NOT_SUITABLE;
        }
        */
    }
}
