package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadOWLDeprecatedClassTestCase extends AbstractJenaTestCase {

    public void testLoadSimpleDeprecatedClass() throws Exception {
        loadRemoteOntology("deprecated.owl");
        RDFSNamedClass carClass = owlModel.getRDFSNamedClass("Car");
        assertNotNull(carClass);
        //assertFalse(carClass instanceof OWLNamedClass);
        assertTrue(carClass.isDeprecated());
    }


    public void testLoadDeprecatedClassWithRestriction() throws Exception {
        loadRemoteOntology("owlDeprecatedClass.owl");
        RDFSNamedClass cls = owlModel.getRDFSNamedClass("Class");
        assertTrue(cls.isDeprecated());
        assertSize(2, cls.getRDFTypes());
        assertContains(owlModel.getOWLDeprecatedClassClass(), cls.getRDFTypes());
        assertContains(owlModel.getOWLNamedClassClass(), cls.getRDFTypes());
        assertTrue(cls instanceof OWLNamedClass);
        dumpRDF();
    }
}
