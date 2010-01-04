package edu.stanford.smi.protegex.owl.tests;

import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import junit.framework.TestCase;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProtegeOWLTestCase extends TestCase {

    public void testLoadFromInputStream() throws Exception {
        InputStream is = new URL(AbstractOWLTestCase.getRemoteOntologyRoot() + "travel.owl").openStream();
        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModelFromInputStream(is);
        assertEquals(34, owlModel.getUserDefinedOWLNamedClasses().size());
    }


    public void testLoadFromReader() throws Exception {
        InputStream is = new URL(AbstractOWLTestCase.getRemoteOntologyRoot() + "travel.owl").openStream();
        Reader reader = new InputStreamReader(is);
        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModelFromReader(reader);
        assertEquals(34, owlModel.getUserDefinedOWLNamedClasses().size());
    }
}
