package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLDatatypePropertyTestCase extends AbstractJenaTestCase {

    public void testDefaultDatatypeProperty() {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        assertFalse(property.isDomainDefined());
        assertTrue(((Slot) property).getDirectDomain().contains(owlThing));
        assertFalse(property.isFunctional());
        assertFalse(property.isInverseFunctional());
        assertFalse(property.isAnnotationProperty());
        assertTrue(((Slot) property).getAllowsMultipleValues());
        assertFalse(property.hasRange(true));
        assertEquals(ValueType.ANY, ((Slot) property).getValueType());
    }


    public void testRange() {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        assertTrue(property.hasDatatypeRange());
        assertFalse(property.hasObjectRange());
    }
}
