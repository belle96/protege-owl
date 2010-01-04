package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;


/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadOWLHasValueTestCase extends AbstractJenaTestCase {

    public void testHasRestriction() throws Exception {
        loadRemoteOntology("hasRestrictionOnDoubleProperty.owl");
    }
}
