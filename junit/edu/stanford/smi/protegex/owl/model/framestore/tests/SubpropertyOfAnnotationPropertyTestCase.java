package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SubpropertyOfAnnotationPropertyTestCase extends AbstractJenaTestCase {

    public void testCreateSubSlot() {
        OWLDatatypeProperty superSlot = owlModel.createAnnotationOWLDatatypeProperty("superSlot");
        OWLDatatypeProperty subSlot = owlModel.createOWLDatatypeProperty("subSlot");
        assertFalse(subSlot.isAnnotationProperty());
        subSlot.addSuperproperty(superSlot);
        assertTrue(subSlot.isAnnotationProperty());
    }
}
