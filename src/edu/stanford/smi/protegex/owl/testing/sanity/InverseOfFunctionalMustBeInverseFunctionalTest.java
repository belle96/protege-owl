package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InverseOfFunctionalMustBeInverseFunctionalTest extends AbstractOWLTest
        implements AutoRepairableOWLTest, RDFPropertyTest {

    public InverseOfFunctionalMustBeInverseFunctionalTest() {
        super(SANITY_GROUP, null);
    }


    public static boolean fails(RDFProperty property) {
        Slot inverseSlot = property.getInverseProperty();
        if (inverseSlot instanceof OWLObjectProperty &&
                property instanceof OWLObjectProperty &&
                ((OWLObjectProperty) property).isFunctional()) {
            return !((OWLObjectProperty) inverseSlot).isInverseFunctional();
        }
        else {
            return false;
        }
    }


    public boolean repair(OWLTestResult testResult) {
        OWLProperty property = (OWLProperty) testResult.getHost();
        OWLObjectProperty inverseSlot = (OWLObjectProperty) property.getInverseProperty();
        inverseSlot.setInverseFunctional(true);
        return true;
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("The inverse of a functional property should be inverse functional.",
                    property,
                    OWLTestResult.TYPE_WARNING,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
