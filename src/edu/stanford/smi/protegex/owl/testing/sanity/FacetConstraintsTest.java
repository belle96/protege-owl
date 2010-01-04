package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.testing.AbstractOWLTest;
import edu.stanford.smi.protegex.owl.testing.DefaultOWLTestResult;
import edu.stanford.smi.protegex.owl.testing.OWLTestResult;
import edu.stanford.smi.protegex.owl.testing.RDFResourceTest;
import edu.stanford.smi.protegex.owl.testing.todo.TodoAnnotationOWLTest;

import java.util.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FacetConstraintsTest extends AbstractOWLTest implements RDFResourceTest {

    public FacetConstraintsTest() {
        super(TodoAnnotationOWLTest.GROUP, null);
    }


    public List test(RDFResource instance) {
        if (instance instanceof RDFIndividual) {
            List violations = new ArrayList();
            Collection slots = instance.getPossibleRDFProperties();
            Iterator k = slots.iterator();
            while (k.hasNext()) {
                Slot slot = (Slot) k.next();
                if (slot instanceof OWLProperty) {
                    Collection values = ((Instance) instance).getDirectOwnSlotValues(slot);
                    if (!((Instance) instance).areValidOwnSlotValues(slot, values)) {
                        String text = ((Instance) instance).getInvalidOwnSlotValuesText(slot, values);
                        violations.add(new DefaultOWLTestResult("Constraint violation at " +
                                slot.getBrowserText() + ": " + text,
                                instance,
                                OWLTestResult.TYPE_WARNING,
                                this));
                    }
                }
            }
            return violations;
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }

}
