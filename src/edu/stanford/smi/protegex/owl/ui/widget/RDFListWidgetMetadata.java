package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFListWidgetMetadata implements OWLWidgetMetadata {

    public int getSuitability(RDFSNamedClass cls, RDFProperty property) {
        if (isSuitableWidget(cls, property)) {
            return SUITABLE;
        }
        else {
            return NOT_SUITABLE;
        }
    }


    // TODO: Make OWL 2.0 compliant
    public boolean isSuitableWidget(RDFSNamedClass cls, RDFProperty property) {
        if (((Cls) cls).getTemplateSlotValueType(property) == ValueType.INSTANCE) {
            Cls listCls = property.getOWLModel().getRDFListClass();
            Collection clses = ((Cls) cls).getTemplateSlotAllowedClses(property);
            for (Iterator it = clses.iterator(); it.hasNext();) {
                Cls allowedCls = (Cls) it.next();
                if (allowedCls.equals(listCls) || allowedCls.hasSuperclass(listCls)) {
                    return true;
                }
            }
        }
        return false;
    }
}
