package edu.stanford.smi.protegex.owl.swrl.parser.tests;

import edu.stanford.smi.protegex.owl.swrl.model.impl.tests.AbstractSWRLTestCase;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ParseVariablesTestCase extends AbstractSWRLTestCase {

    public void testCreateAndReuse() throws Exception {
        owlModel.createOWLNamedClass("Cls");
        factory.createImp("Cls(?x) " + SWRLParser.AND_CHAR + " Cls(?y) " + SWRLParser.IMP_CHAR + " Cls(?y)");
        assertSize(2, factory.getVariables());
        assertNotNull(factory.getVariable("x"));
        assertNotNull(factory.getVariable("y"));
        factory.createImp("Cls(?x) " + SWRLParser.IMP_CHAR + " Cls(?x)");
        assertSize(2, factory.getVariables());
        assertNotNull(factory.getVariable("x"));
    }
}
