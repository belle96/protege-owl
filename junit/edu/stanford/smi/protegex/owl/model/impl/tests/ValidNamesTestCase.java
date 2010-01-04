package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLModel;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ValidNamesTestCase extends AbstractJenaTestCase {

    public void testValidNames() {
        assertConvert("Normal", "Normal");
        assertConvert("_start", "@start");
        assertConvert("end_", "end@");
        assertConvert("mul_ti_ple", "mul@ti@ple");
    }


    private void assertConvert(String expected, String input) {
        assertEquals(expected, AbstractOWLModel.getValidOWLFrameName(owlModel, input));
    }
}
