package edu.stanford.smi.protegex.owl.swrl.model.impl.tests;

import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.factory.SWRLJavaFactory;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractSWRLTestCase extends AbstractJenaTestCase {

    protected SWRLFactory factory;


    protected void setUp() throws Exception {
        super.setUp();
        loadRemoteOntology("importSWRL.owl");
        assertTrue(owlModel.getFrameFactory() instanceof SWRLJavaFactory);
        factory = new SWRLFactory(owlModel);
    }
}
