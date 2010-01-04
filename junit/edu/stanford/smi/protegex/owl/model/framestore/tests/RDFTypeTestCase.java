package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFTypeTestCase extends AbstractJenaTestCase {


    public void testAddAndRemoveDirectType() {
        OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
        OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
        OWLIndividual individual = clsA.createOWLIndividual("instance");
        assertSize(1, individual.getRDFTypes());
        individual.addProtegeType(clsB);
        assertSize(2, individual.getRDFTypes());
        assertContains(clsA, individual.getRDFTypes());
        assertContains(clsB, individual.getRDFTypes());
        individual.removeProtegeType(clsA);
        assertSize(1, individual.getRDFTypes());
        assertContains(clsB, individual.getRDFTypes());
    }


    public void testCreateOWLEnumeratedClass() {
        OWLEnumeratedClass enumeratedClass = owlModel.createOWLEnumeratedClass();
        assertSize(1, enumeratedClass.getRDFTypes());
        assertEquals(owlModel.getOWLNamedClassClass(), enumeratedClass.getRDFType());
    }


    public void testCreateOWLLogicalClass() {
        OWLComplementClass cls = owlModel.createOWLComplementClass(owlThing);
        assertSize(1, cls.getRDFTypes());
        assertEquals(owlModel.getOWLNamedClassClass(), cls.getRDFType());
    }


    public void testCreateOWLRestriction() {
        RDFProperty property = owlModel.createRDFProperty("property");
        OWLRestriction restriction = owlModel.createOWLCardinality(property, 1);
        assertSize(1, restriction.getRDFTypes());
        assertEquals(owlModel.getRDFSNamedClass(OWLNames.Cls.RESTRICTION), restriction.getRDFType());
    }


    public void testCreateOWLNamedClass() {
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Test");
        assertSize(1, cls.getRDFTypes());
        assertEquals(owlModel.getRDFSNamedClassClass(), cls.getRDFType());
    }


    public void testCreateOWLIndividual() {
        RDFIndividual individual = owlThing.createOWLIndividual("Test");
        assertSize(1, individual.getRDFTypes());
        assertEquals(owlThing, individual.getRDFType());
    }


    public void testCreateRDFProperty() {
        RDFProperty property = owlModel.createOWLObjectProperty("Test");
        assertSize(1, property.getRDFTypes());
        assertEquals(owlModel.getOWLObjectPropertyClass(), property.getRDFType());
    }
}
