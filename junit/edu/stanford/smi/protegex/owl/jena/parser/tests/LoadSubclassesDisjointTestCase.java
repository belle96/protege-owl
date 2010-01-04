package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadSubclassesDisjointTestCase extends AbstractJenaTestCase {

    public void testLoadSubclassesDisjoint() throws Exception {
        loadRemoteOntology("subclassesDisjoint.owl");
        OWLNamedClass b = owlModel.getOWLNamedClass("B");
        assertSize(2, b.getDisjointClasses());
    }
}
