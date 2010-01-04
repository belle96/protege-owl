package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLInverseOfTestCase extends AbstractJenaTestCase {

    public void testAddInverseSlot() {
        RDFProperty owlInverseOfProperty = owlModel.getRDFProperty(OWLNames.Slot.INVERSE_OF);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLObjectProperty inverse = owlModel.createOWLObjectProperty("inverse");
        assertNull(property.getPropertyValue(owlInverseOfProperty));
        assertNull(inverse.getPropertyValue(owlInverseOfProperty));
        property.setInverseProperty(inverse);
        assertEquals(inverse, property.getPropertyValue(owlInverseOfProperty));
        property.setInverseProperty(null);
        assertNull(property.getPropertyValue(owlInverseOfProperty));
    }
}
