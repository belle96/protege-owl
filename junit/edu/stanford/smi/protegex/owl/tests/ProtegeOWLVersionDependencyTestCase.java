package edu.stanford.smi.protegex.owl.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;
import edu.stanford.smi.protege.resource.Text;
import edu.stanford.smi.protege.util.FileUtilities;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.resource.OWLText;

public class ProtegeOWLVersionDependencyTestCase extends TestCase {

    private static Properties props;

    private static String buildFile = "resource/files/build.properties";

    private static String jena_major = "prowl.dependencies.jena.major_version";

    private static String jena_minor = "prowl.dependencies.jena.major_version";

    private static String protege_version = "build.version";


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        try {
            props = new Properties();
            InputStream stream = FileUtilities.getResourceStream(ProtegeOWL.class, buildFile);
            props.load(stream);
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }


    /**
     * @throws Exception
     * @todo Compare actual vs. expected versions
     */
    public void testShowVersionDependencies() throws Exception {
        assertEquals(protege_version, Text.getVersion(), OWLText.getVersion());
    }

}
