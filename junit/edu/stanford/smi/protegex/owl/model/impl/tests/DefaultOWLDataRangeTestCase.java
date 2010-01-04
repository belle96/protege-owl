package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLDataRange;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLDataRangeTestCase extends AbstractJenaTestCase {

    public void testBrowserText() {
        OWLDataRange dataRange = owlModel.createOWLDataRange(new RDFSLiteral[]{
                owlModel.createRDFSLiteral("A"),
                owlModel.createRDFSLiteral("B")
        });
        assertEquals("owl:oneOf{\"A\" \"B\"}", dataRange.getBrowserText());
    }
}
