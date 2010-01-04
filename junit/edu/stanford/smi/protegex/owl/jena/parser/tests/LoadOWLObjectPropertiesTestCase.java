package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadOWLObjectPropertiesTestCase extends AbstractJenaTestCase {

    public void testLoadInverseTransitive() throws Exception {
        OWLObjectProperty oldProperty = owlModel.createOWLObjectProperty("property");
        OWLObjectProperty oldInverse = owlModel.createOWLObjectProperty("inverse");
        oldProperty.setInverseProperty(oldInverse);
        oldProperty.setTransitive(true);
        oldInverse.setTransitive(true);
        OWLModel newModel = reload(owlModel);
        OWLObjectProperty newProperty = newModel.getOWLObjectProperty(oldProperty.getName());
        OWLObjectProperty newInverse = newModel.getOWLObjectProperty(oldInverse.getName());
        assertTrue(newProperty.isTransitive());
        assertTrue(newInverse.isTransitive());
        assertEquals(newProperty, newInverse.getInverseProperty());
        assertEquals(newInverse, newProperty.getInverseProperty());
        assertSize(2, newProperty.getRDFTypes());
        assertSize(2, newInverse.getRDFTypes());
        assertSize(2, ((Slot) newProperty).getDirectTypes());
        assertSize(2, ((Slot) newInverse).getDirectTypes());
    }
}
