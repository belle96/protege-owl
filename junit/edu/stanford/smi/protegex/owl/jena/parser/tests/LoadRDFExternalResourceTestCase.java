package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadRDFExternalResourceTestCase extends AbstractJenaTestCase {

    public void testLoadExternalSeeAlso() throws Exception {
        loadRemoteOntology("externalSeeAlso.owl");
        OWLNamedClass cls = owlModel.getOWLNamedClass("Cls");
        assertNotNull(cls);
        Instance instance = owlModel.getOWLIndividual("Instance");
        assertNotNull(instance);
        RDFProperty seeAlsoSlot = owlModel.getRDFProperty(RDFSNames.Slot.SEE_ALSO);
        RDFProperty isDefinedBySlot = owlModel.getRDFProperty(RDFSNames.Slot.IS_DEFINED_BY);

        Object seeAlsoValue = cls.getPropertyValue(seeAlsoSlot);
        assertTrue(seeAlsoValue instanceof RDFResource);
        RDFResource ur = (RDFResource) seeAlsoValue;
        assertEquals("http://aldi.de", ur.getURI());

        Object isDefinedByValue = cls.getPropertyValue(isDefinedBySlot);
        assertEquals(instance, isDefinedByValue);

        assertEquals(0, ur.getRDFTypes().size());
        assertEquals(1, ur.getProtegeTypes().size());
        assertEquals(owlModel.getSystemFrames().getRdfExternalResourceClass(), ur.getProtegeType());
    }
}
