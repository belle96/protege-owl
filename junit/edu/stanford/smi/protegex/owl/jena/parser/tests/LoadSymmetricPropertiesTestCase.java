package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadSymmetricPropertiesTestCase extends AbstractJenaTestCase {


    public void testLoadSymmetricProperty() throws Exception {
        loadRemoteOntology("symmetric-only.owl");
        OWLNamedClass cls = owlModel.getOWLNamedClass("Human");
        assertNotNull(cls);
        OWLObjectProperty friendOfSlot = owlModel.getOWLObjectProperty("friendOf");
        assertNotNull(friendOfSlot);
        assertEquals(cls, friendOfSlot.getUnionDomain().iterator().next());
        assertEquals(cls, friendOfSlot.getUnionRangeClasses().iterator().next());
    }


    public void testLoadTransitiveProperty() throws Exception {
        loadRemoteOntology("symmetric-only.owl");
        OWLObjectProperty partOfSlot = (OWLObjectProperty) owlModel.getRDFProperty("partOf");
        assertNotNull(partOfSlot);
    }
}
