package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InverseOfTopLevelPropertyMustBeTopLevelPropertyTest extends AbstractOWLTest
        implements RDFPropertyTest, AutoRepairableOWLTest {

    public InverseOfTopLevelPropertyMustBeTopLevelPropertyTest() {
        super(SANITY_GROUP, null);
    }


    public static boolean fails(RDFProperty property) {
        if (property instanceof OWLObjectProperty) {
            Slot inverseSlot = property.getInverseProperty();
            return inverseSlot != null &&
                    property.getSuperpropertyCount() == 0 &&
                    inverseSlot.getDirectSuperslotCount() > 0;
        }
        return false;
    }


    public boolean repair(OWLTestResult testResult) {
        return repair((OWLProperty) testResult.getHost());
    }


    public static boolean repair(OWLProperty property) {
        if (property instanceof OWLObjectProperty) {
            Slot inverseSlot = property.getInverseProperty();
            if (inverseSlot != null) {
                for (Iterator it = new ArrayList(inverseSlot.getDirectSuperslots()).iterator(); it.hasNext();) {
                    Slot superSlot = (Slot) it.next();
                    inverseSlot.removeDirectSuperslot(superSlot);
                }
                return !fails(property);
            }
        }
        return false;
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("The inverse of a top-level property should also be top-level property.",
                    property,
                    OWLTestResult.TYPE_WARNING,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
