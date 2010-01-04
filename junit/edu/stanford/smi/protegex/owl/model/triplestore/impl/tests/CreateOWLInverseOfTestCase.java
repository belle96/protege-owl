package edu.stanford.smi.protegex.owl.model.triplestore.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateOWLInverseOfTestCase extends AbstractTripleStoreTestCase {


    public void testAddInverse() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLObjectProperty inverse = owlModel.createOWLObjectProperty("inverse");
        ts.add(property, owlModel.getRDFProperty(OWLNames.Slot.INVERSE_OF), inverse);
        assertEquals(inverse, property.getInverseProperty());
        assertEquals(property, inverse.getInverseProperty());
    }
}
