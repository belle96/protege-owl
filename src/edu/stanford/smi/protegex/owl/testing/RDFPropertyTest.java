package edu.stanford.smi.protegex.owl.testing;

import edu.stanford.smi.protegex.owl.model.RDFProperty;

import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFPropertyTest extends OWLTest {

    /**
     * Performs a check of a given property.
     *
     * @param property the RDFProperty to check
     * @return a list of OWLTestResult objects
     */
    List test(RDFProperty property);
}
