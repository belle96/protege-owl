package edu.stanford.smi.protegex.owl.model.event.tests;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.event.ResourceAdapter;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ResourceListenerTestCase extends AbstractJenaTestCase {


    /**
     * A test reported by Matthew: The rdf:type is not assigned yet when the event is sent.
     */
    public void testGetRFDFTypes() {
        OWLNamedClass clsA = owlModel.createOWLNamedClass("ClsA");
        OWLIndividual indA = owlModel.getOWLThingClass().createOWLIndividual("indA");
        owlModel.addResourceListener(new ResourceAdapter() {
            public void typeAdded(RDFResource rdfResource,
                                  RDFSClass rdfsClass) {
                assertTrue(rdfResource.getProtegeTypes().contains(rdfsClass));
                assertTrue(rdfResource.getRDFTypes().contains(rdfsClass));
            }
        });
        indA.addRDFType(clsA);
    }
}
