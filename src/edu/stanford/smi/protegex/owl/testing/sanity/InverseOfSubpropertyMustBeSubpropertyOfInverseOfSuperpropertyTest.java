package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest extends AbstractOWLTest
        implements RDFPropertyTest, AutoRepairableOWLTest {

    public InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest() {
        super(SANITY_GROUP, null);
    }


    public static boolean fails(RDFProperty property) {
        if (property instanceof OWLObjectProperty) {
            RDFProperty inverseProperty = property.getInverseProperty();
            if (inverseProperty != null && property.getSuperpropertyCount() == 1 && !property.equals(inverseProperty)) {
                RDFProperty parentProperty = (RDFProperty) property.getSuperproperties(false).iterator().next();
                RDFProperty parentInverse = parentProperty.getInverseProperty();
                if (parentInverse != null) {
                    return !inverseProperty.equals(parentInverse) &&
                            !inverseProperty.isSubpropertyOf(parentInverse, true);
                }
            }
        }
        return false;
    }


    public boolean repair(OWLTestResult testResult) {
        return repair((OWLProperty) testResult.getHost());
    }


    public static boolean repair(OWLProperty property) {
        Slot inverseSlot = property.getInverseProperty();
        if (inverseSlot != null &&
                property.getSuperpropertyCount() == 1 &&
                property instanceof OWLObjectProperty) {
            Slot parentSlot = (Slot) property.getSuperproperties(false).iterator().next();
            Slot parentInverse = parentSlot.getInverseSlot();
            if (parentInverse != null) {
                for (Iterator it = inverseSlot.getDirectSuperslots().iterator(); it.hasNext();) {
                    Slot superSlot = (Slot) it.next();
                    inverseSlot.removeDirectSuperslot(superSlot);
                }
                inverseSlot.addDirectSuperslot(parentInverse);
                return !fails(property);
            }
        }
        return false;
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("The inverse of a property should be a true sub-property of the inverse of its super-property.",
                    property,
                    OWLTestResult.TYPE_WARNING,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
