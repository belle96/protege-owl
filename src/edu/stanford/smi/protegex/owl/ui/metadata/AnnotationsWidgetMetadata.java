package edu.stanford.smi.protegex.owl.ui.metadata;

import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.widget.OWLWidgetMetadata;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AnnotationsWidgetMetadata implements OWLWidgetMetadata {

    public int getSuitability(RDFSNamedClass cls, RDFProperty property) {
        if (property.getName().equals(Model.Slot.DIRECT_TYPES) ||
                property.getName().equals(ProtegeNames.Slot.INFERRED_TYPE)) {  // Any unused, visible slot
            return SUITABLE;
        }
        else {
            return NOT_SUITABLE;
        }
    }
}
