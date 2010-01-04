package edu.stanford.smi.protegex.owl.testing;

import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFResourceTest extends OWLTest {

    /**
     * Performs a check of a given RDFResource.
     *
     * @param instance the RDFResource to check
     * @return a list of OWLTestResult objects
     */
    List test(RDFResource instance);
}
