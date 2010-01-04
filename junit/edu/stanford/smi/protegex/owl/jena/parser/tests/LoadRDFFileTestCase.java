package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.net.URI;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadRDFFileTestCase extends AbstractJenaTestCase {

    public void testLoadFOAF() throws Exception {
        //loadTestOntology(new URI("http://xmlns.com/foaf/0.1/index.rdf"));
    	loadTestOntology(new URI("http://xmlns.com/foaf/spec/index.rdf"));
        owlModel.getOntModel();
    }


    public void testLoadICalOntology() throws Exception {
        loadTestOntology(new URI("http://www.w3.org/2002/12/cal/ical"));
        owlModel.getOntModel();
    }


    public void testLoadRDF() throws Exception {
        loadRemoteOntology("rdf-test.owl");
        RDFSNamedClass animalCls = owlModel.getRDFSNamedClass("Animal");
        assertTrue(animalCls instanceof RDFSNamedClass);
        assertFalse(animalCls instanceof OWLNamedClass);
        RDFProperty hasChildrenProperty = owlModel.getRDFProperty("hasChildren");
        assertTrue(hasChildrenProperty instanceof RDFProperty);
        assertFalse(hasChildrenProperty instanceof OWLProperty);
        RDFIndividual purzel = owlModel.getRDFIndividual("Purzel");
        assertEquals(animalCls, purzel.getProtegeType());
        Instance susie = owlModel.getRDFIndividual("Susie");
        assertEquals(animalCls, susie.getDirectType());
        assertSize(1, purzel.getPropertyValues(hasChildrenProperty));
        assertEquals(susie, purzel.getPropertyValue(hasChildrenProperty));
    }
}
