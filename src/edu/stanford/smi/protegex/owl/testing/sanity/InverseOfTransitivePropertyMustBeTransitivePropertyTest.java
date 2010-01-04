package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InverseOfTransitivePropertyMustBeTransitivePropertyTest extends AbstractOWLTest
        implements RDFPropertyTest, RepairableOWLTest {

    public InverseOfTransitivePropertyMustBeTransitivePropertyTest() {
        super(SANITY_GROUP, null);
    }


    public static boolean fails(RDFProperty property) {
        if (property instanceof OWLObjectProperty && property.getInverseProperty() instanceof OWLObjectProperty) {
            OWLObjectProperty inverseSlot = (OWLObjectProperty) property.getInverseProperty();
            return ((OWLObjectProperty) property).isTransitive() != inverseSlot.isTransitive();
        }
        return false;
    }


    public boolean repair(OWLTestResult testResult) {
        RDFProperty property = (RDFProperty) testResult.getHost();
        if (fails(property)) {
            if (property instanceof OWLObjectProperty && property.getInverseProperty() instanceof OWLObjectProperty) {
                OWLObjectProperty inverseProperty = (OWLObjectProperty) property.getInverseProperty();
                boolean transitive = ((OWLObjectProperty) property).isTransitive();
                inverseProperty.setTransitive(transitive);
                return true;
            }
        }
        return false;
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("The transitivity of a property should also hold for its inverse.",
                    property,
                    OWLTestResult.TYPE_WARNING,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
