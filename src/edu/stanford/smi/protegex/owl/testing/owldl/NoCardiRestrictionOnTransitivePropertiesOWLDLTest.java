package edu.stanford.smi.protegex.owl.testing.owldl;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLCardinalityBase;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class NoCardiRestrictionOnTransitivePropertiesOWLDLTest extends AbstractOWLTest implements OWLDLTest, RDFSClassTest {

    public NoCardiRestrictionOnTransitivePropertiesOWLDLTest() {
        super(GROUP, null);
    }


    public static boolean fails(RDFSClass aClass) {
        if (aClass instanceof OWLCardinalityBase) {
            OWLCardinalityBase r = (OWLCardinalityBase) aClass;
            Slot slot = r.getOnProperty();
            return isTransitive(slot, new HashSet());
        }
        return false;
    }


    private static boolean isTransitive(Slot slot, Set reached) {
        if (!reached.contains(slot)) {
            reached.add(slot);
            if (slot instanceof OWLObjectProperty) {
                OWLObjectProperty objectSlot = (OWLObjectProperty) slot;
                if (objectSlot.isTransitive()) {
                    return true;
                }
                for (Iterator it = slot.getDirectSubslots().iterator(); it.hasNext();) {
                    Slot subSlot = (Slot) it.next();
                    if (isTransitive(subSlot, reached)) {
                        return true;
                    }
                }
                Slot inverseSlot = slot.getInverseSlot();
                if (inverseSlot != null && isTransitive(inverseSlot, reached)) {
                    return true;
                }
            }
        }
        return false;
    }


    public List test(RDFSClass aClass) {
        if (fails(aClass)) {
            return Collections.singletonList(new DefaultOWLTestResult("Cardinality restrictions on transitive properties (or inverse or super properties of them) are not allowed in OWL DL.",
                    aClass,
                    OWLTestResult.TYPE_OWL_FULL,
                    this));
        }
        return Collections.EMPTY_LIST;
    }
}
