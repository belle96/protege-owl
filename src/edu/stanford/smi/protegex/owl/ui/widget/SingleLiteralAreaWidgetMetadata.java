package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SingleLiteralAreaWidgetMetadata implements OWLWidgetMetadata {

    public int getSuitability(RDFSNamedClass cls, RDFProperty property) {
        if ((OWLWidgetUtil.isDatatypeProperty(cls.getOWLModel().getXSDstring(), cls, property) ||
                OWLWidgetUtil.isDatatypeProperty(cls.getOWLModel().getRDFXMLLiteralType(), cls, property)) &&
                OWLWidgetUtil.isFunctionalProperty(cls, property)) {
            return SUITABLE;
        }
        else {
            return NOT_SUITABLE;
        }
    }
}
