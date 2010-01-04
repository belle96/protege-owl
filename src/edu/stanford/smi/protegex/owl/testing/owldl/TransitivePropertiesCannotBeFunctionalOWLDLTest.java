package edu.stanford.smi.protegex.owl.testing.owldl;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TransitivePropertiesCannotBeFunctionalOWLDLTest extends AbstractOWLTest
        implements OWLDLTest, RDFPropertyTest, RepairableOWLTest {

    public TransitivePropertiesCannotBeFunctionalOWLDLTest() {
        super(GROUP, null);
    }


    public static boolean fails(RDFProperty property) {
        if (property instanceof OWLObjectProperty && ((OWLObjectProperty) property).isTransitive()) {
            return isFunctional(property, new HashSet());
        }
        return false;
    }


    private static boolean isFunctional(Slot slot, Set reached) {
        if (!reached.contains(slot)) {
            reached.add(slot);
            if (slot instanceof OWLObjectProperty) {
                OWLObjectProperty objectSlot = (OWLObjectProperty) slot;
                if (objectSlot.isFunctional() || objectSlot.isInverseFunctional()) {
                    return true;
                }
                for (Iterator it = slot.getDirectSuperslots().iterator(); it.hasNext();) {
                    Slot superSlot = (Slot) it.next();
                    if (isFunctional(superSlot, reached)) {
                        return true;
                    }
                }
                Slot inverseSlot = slot.getInverseSlot();
                if (inverseSlot != null && isFunctional(inverseSlot, reached)) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean repair(OWLTestResult testResult) {
        OWLProperty property = (OWLProperty) testResult.getHost();
        property.setFunctional(false);
        property.setInverseFunctional(false);
        return !fails(property);
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("Transitive properties (or inverse or super properties of them) cannot be functional (or inverse functional) in OWL DL.",
                                                                      property,
                                                                      OWLTestResult.TYPE_OWL_FULL,
                                                                      this));
        }
        return Collections.EMPTY_LIST;
    }
}
