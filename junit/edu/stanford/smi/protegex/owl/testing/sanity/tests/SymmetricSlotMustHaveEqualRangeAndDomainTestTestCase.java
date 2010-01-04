package edu.stanford.smi.protegex.owl.testing.sanity.tests;

import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.testing.sanity.SymmetricPropertyMustHaveEqualRangeAndDomainTest;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SymmetricSlotMustHaveEqualRangeAndDomainTestTestCase extends AbstractJenaTestCase {

    public void testThing() {
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        slot.setDomainDefined(false);
        slot.setSymmetric(true);
        assertFalse(SymmetricPropertyMustHaveEqualRangeAndDomainTest.fails(slot));
    }
}
