package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.validator.DefaultPropertyValueValidator;
import edu.stanford.smi.protegex.owl.model.validator.PropertyValueValidator;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyValueValidatorTestCase extends AbstractJenaTestCase {

    public void testDefaultValidator() {
        assertTrue(owlModel.getPropertyValueValidator() instanceof DefaultPropertyValueValidator);
    }


    public void testSetAndGet() {
        PropertyValueValidator v = new PropertyValueValidator() {
            public boolean isValidPropertyValue(RDFResource subject, RDFProperty predicate, Object object) {
                return false;
            }
        };
        owlModel.setPropertyValueValidator(v);
        assertSame(v, owlModel.getPropertyValueValidator());
    }
}
