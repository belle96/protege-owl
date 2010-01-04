package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class MultilingualBrowserTextTestCase extends AbstractJenaTestCase {

    public void testDefaultBrowserText() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        RDFIndividual instance = cls.createRDFIndividual("Instance");
        assertEquals(instance.getLocalName(), instance.getBrowserText());
    }


    public void testLabelBrowserText() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        RDFResource individual = namedClass.createRDFIndividual("Instance");
        namedClass.setDirectBrowserSlot(owlModel.getRDFSLabelProperty());
        individual.addLabel("inst", null);
        individual.addLabel("deutsch", "de");
        assertEquals("inst", individual.getBrowserText());
        RDFProperty metaSlot = owlModel.getRDFProperty(ProtegeNames.getDefaultLanguageSlotName());
        OWLOntology oi = owlModel.getDefaultOWLOntology();
        oi.setPropertyValue(metaSlot, "de");
        assertEquals("deutsch", individual.getBrowserText());
    }
}
