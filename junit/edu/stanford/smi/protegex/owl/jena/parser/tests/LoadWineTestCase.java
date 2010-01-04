package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadWineTestCase extends AbstractJenaTestCase {

    public void testLoadWineOntology() throws Exception {
        loadTestOntology(new URI("http://www.w3.org/TR/2004/REC-owl-guide-20040210/wine.rdf"));
    }


    /**
     * OWL File structure:
     * owl:Thing
     * Physik
     * Typen
     * Theoretische_Physik
     * Thermodynamik
     * Gebiete
     * Thermodynamik
     */
    public void testLoadSuperclasses() throws Exception {
        loadRemoteOntology("andreas.owl");
        Collection errors = new ArrayList();
        assertEquals(0, errors.size());
        OWLNamedClass typenCls = owlModel.getOWLNamedClass("Typen");
        OWLNamedClass theoCls = owlModel.getOWLNamedClass("Theoretische_Physik");
        OWLNamedClass physikCls = owlModel.getOWLNamedClass("Physik");
        OWLNamedClass gebieteCls = owlModel.getOWLNamedClass("Gebiete");
        OWLNamedClass thermoCls = owlModel.getOWLNamedClass("Thermodynamik");
        assertTrue(thermoCls.getSuperclasses(false).contains(theoCls));
        assertTrue(thermoCls.getSuperclasses(false).contains(gebieteCls));
        assertTrue(thermoCls.getSuperclasses(false).contains(physikCls));
        assertEquals(3, thermoCls.getSuperclassCount());
    }
}
