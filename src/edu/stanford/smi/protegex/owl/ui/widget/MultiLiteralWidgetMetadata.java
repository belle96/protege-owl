package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class MultiLiteralWidgetMetadata implements OWLWidgetMetadata {

    public int getSuitability(RDFSNamedClass cls, RDFProperty property) {
        if (isPreferredWidget(cls, property)) {
            return DEFAULT;
        }
        else if (isSuitableWidget(cls, property)) {
            return SUITABLE;
        }
        else {
            return NOT_SUITABLE;
        }
    }


    public boolean isPreferredWidget(RDFSNamedClass cls, RDFProperty property) {
        if (OWLWidgetUtil.isRangelessDatatypeProperty(cls, property)) {
            return !OWLWidgetUtil.isFunctionalProperty(cls, property);
        }
        else {
            return false;
        }
    }


    public boolean isSuitableWidget(RDFSNamedClass cls, RDFProperty property) {
        return property.isPureAnnotationProperty() || 
            (OWLWidgetUtil.isDatatypeProperty(cls, property) &&
                !OWLWidgetUtil.isFunctionalProperty(cls, property));
    }
}
