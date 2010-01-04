package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InverseOfSymmetricPropertyMustBeSymmetricPropertyTest extends AbstractOWLTest
        implements RDFPropertyTest, AutoRepairableOWLTest {

    public InverseOfSymmetricPropertyMustBeSymmetricPropertyTest() {
        super(SANITY_GROUP, null);
    }


    public static boolean fails(RDFProperty property) {
        if (property instanceof OWLObjectProperty && property.getInverseProperty() instanceof OWLObjectProperty) {
            OWLObjectProperty inverseSlot = (OWLObjectProperty) property.getInverseProperty();
            return ((OWLObjectProperty) property).isSymmetric() != inverseSlot.isSymmetric();
        }
        return false;
    }


    public boolean repair(OWLTestResult testResult) {
        Slot slot = (Slot) testResult.getHost();
        if (slot instanceof OWLObjectProperty && slot.getInverseSlot() instanceof OWLObjectProperty) {
            OWLObjectProperty inverseSlot = (OWLObjectProperty) slot.getInverseSlot();
            inverseSlot.setSymmetric(((OWLObjectProperty) slot).isSymmetric());
            return true;
        }
        return false;
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("The symmetricity of a property should also hold for its inverse.",
                    property,
                    OWLTestResult.TYPE_WARNING,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
