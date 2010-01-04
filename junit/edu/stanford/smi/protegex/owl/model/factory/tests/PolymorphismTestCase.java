package edu.stanford.smi.protegex.owl.model.factory.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PolymorphismTestCase extends AbstractJenaTestCase {


    public void testSystemResources() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        assertTrue(cls.canAs(OWLNamedClass.class));
        assertTrue(cls.canAs(RDFSNamedClass.class));
        assertTrue(cls.canAs(RDFResource.class));
        assertFalse(cls.canAs(OWLAnonymousClass.class));
        assertFalse(cls.canAs(OWLObjectProperty.class));
        assertTrue(cls.as(RDFSNamedClass.class) instanceof OWLNamedClass);
    }


    public void testPerson() {
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("TestPerson");
        RDFIndividual individual = cls.createRDFIndividual("MyPerson");
        assertTrue(individual.canAs(TestPerson.class));
        RDFResource cast = individual.as(TestPerson.class);
        assertTrue(cast instanceof TestPerson);
        assertEquals(individual, cast);
    }
}
