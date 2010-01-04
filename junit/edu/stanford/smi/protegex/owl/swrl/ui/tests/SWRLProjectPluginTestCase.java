package edu.stanford.smi.protegex.owl.swrl.ui.tests;

import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.swrl.model.factory.SWRLJavaFactory;
import edu.stanford.smi.protegex.owl.swrl.ui.SWRLProjectPlugin;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SWRLProjectPluginTestCase extends AbstractJenaTestCase {

    public void testFrameFactoryIsInstalled() throws Exception {
        JenaKnowledgeBaseFactory.useStandalone = false;
        loadRemoteOntology("importSWRL.owl");
        new SWRLProjectPlugin().afterLoad(owlModel.getProject());
        assertTrue(owlModel.getFrameFactory() instanceof SWRLJavaFactory);
    }
}
