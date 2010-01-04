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
public class InverseOfInverseFunctionalMustBeFunctionalTest extends AbstractOWLTest
        implements AutoRepairableOWLTest, RDFPropertyTest {

    public InverseOfInverseFunctionalMustBeFunctionalTest() {
        super(SANITY_GROUP, null);
    }


    public static boolean fails(RDFProperty property) {
        RDFProperty inverseProperty = property.getInverseProperty();
        if (inverseProperty != null &&
                property instanceof OWLObjectProperty &&
                ((OWLObjectProperty) property).isInverseFunctional()) {
            return !inverseProperty.isFunctional();
        }
        else {
            return false;
        }
    }


    public boolean repair(OWLTestResult testResult) {
        OWLProperty property = (OWLProperty) testResult.getHost();
        Slot inverseSlot = property.getInverseProperty();
        inverseSlot.setAllowsMultipleValues(false);
        return true;
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("The inverse of an inverse functional property should be functional, i.e. it may not have multiple values.",
                    property,
                    OWLTestResult.TYPE_WARNING,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
