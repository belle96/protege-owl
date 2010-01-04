package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class MultiResourceWidgetMetadata implements OWLWidgetMetadata {

    public int getSuitability(RDFSNamedClass cls, RDFProperty property) {
        if (property.isPureAnnotationProperty()) {
            return DEFAULT;
        }
        else if (!(property instanceof OWLDatatypeProperty) &&
                        !(property.getRange() instanceof RDFSDatatype) &&
                        !OWLWidgetUtil.isFunctionalProperty(cls, property)) {
            return DEFAULT;
        }
        else {
            return NOT_SUITABLE;
        }
    }
}
