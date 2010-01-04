package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protegex.owl.model.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DataRangeFieldWidgetMetadata implements OWLWidgetMetadata {

    public int getSuitability(RDFSNamedClass type, RDFProperty predicate) {
        if (type instanceof OWLNamedClass) {
            final OWLNamedClass namedClass = ((OWLNamedClass) type);
            int maxCardinality = namedClass.getMaxCardinality(predicate);
            if (maxCardinality >= 0 && maxCardinality <= 1) {
                RDFResource allValuesFrom = namedClass.getAllValuesFrom(predicate);
                if (allValuesFrom instanceof OWLDataRange) {
                    return DEFAULT;
                }
                RDFResource someValuesFrom = namedClass.getSomeValuesFrom(predicate);
                if (someValuesFrom instanceof OWLDataRange) {
                    return DEFAULT;
                }
            }
        }
        if (predicate.getRange() instanceof OWLDataRange && predicate.isFunctional()) {
            return DEFAULT;
        }
        else {
            return NOT_SUITABLE;
        }
    }
}
