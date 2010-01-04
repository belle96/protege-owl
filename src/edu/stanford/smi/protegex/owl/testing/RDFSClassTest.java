package edu.stanford.smi.protegex.owl.testing;

import edu.stanford.smi.protegex.owl.model.RDFSClass;

import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFSClassTest extends OWLTest {

    /**
     * Performs a check of a given RDFSClass.
     *
     * @param aClass the RDFSClass to check
     * @return a list of OWLTestResult objects
     */
    List test(RDFSClass aClass);
}
