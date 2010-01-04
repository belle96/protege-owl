package edu.stanford.smi.protegex.owl.model.triplestore.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ChangeRDFTypeTestCase extends AbstractJenaTestCase {

    public void testAddRDFType() {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        assertFalse(property.isFunctional());
        RDFSNamedClass type = owlModel.getOWLFunctionalPropertyClass();
        owlModel.getTripleStoreModel().getActiveTripleStore().add(property, owlModel.getRDFTypeProperty(), type);
        assertTrue(property.isFunctional());
        assertContains(property, owlModel.getOWLDatatypePropertyClass().getInstances(false));
        assertContains(property, owlModel.getOWLFunctionalPropertyClass().getInstances(false));
    }


    public void testRemoveRDFType() {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        property.setFunctional(true);
        assertTrue(property.isFunctional());
        RDFSNamedClass type = owlModel.getOWLFunctionalPropertyClass();
        owlModel.getTripleStoreModel().getActiveTripleStore().remove(property, owlModel.getRDFTypeProperty(), type);
        assertFalse(property.isFunctional());
        assertContains(property, owlModel.getOWLDatatypePropertyClass().getInstances(false));
        assertFalse(owlModel.getOWLFunctionalPropertyClass().getInstances(false).contains(property));
    }
}
