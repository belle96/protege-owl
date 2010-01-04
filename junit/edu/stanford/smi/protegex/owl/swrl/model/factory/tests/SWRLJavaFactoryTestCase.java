package edu.stanford.smi.protegex.owl.swrl.model.factory.tests;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltin;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable;
import edu.stanford.smi.protegex.owl.swrl.model.factory.SWRLJavaFactory;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SWRLJavaFactoryTestCase extends AbstractJenaTestCase {


    public void testFrameFactoryInstalled() throws Exception {
        importSWRL();
        assertTrue(owlModel.getFrameFactory() instanceof SWRLJavaFactory);
    }


    private void importSWRL() throws Exception {
        loadRemoteOntology("importSWRL.owl");
    }


    public void testSWRLBuiltin() throws Exception {
        importSWRL();
        SWRLFactory factory = new SWRLFactory(owlModel);
        SWRLBuiltin builtin = factory.createBuiltin("test");
    }


    public void testSWRLVariable() throws Exception {
        importSWRL();
        RDFSNamedClass variableClass = owlModel.getRDFSNamedClass(SWRLNames.Cls.VARIABLE);
        RDFSNamedClass myVariableClass = owlModel.createRDFSNamedSubclass("MyVariable", variableClass);
        RDFResource myVariable = myVariableClass.createInstance("x");
        assertTrue(myVariable instanceof SWRLVariable);
    }
}
