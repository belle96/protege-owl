package edu.stanford.smi.protegex.owl.model.impl.tests;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.model.OWLAllDifferent;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.factory.AlreadyImportedException;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLModelGetOWLIndividualsTestCase extends AbstractJenaTestCase {

    @SuppressWarnings("unchecked")
    public void testDefaultOWLIndividuals() {
        Collection indis = owlModel.getOWLIndividuals();
        assertSize(0, indis);
    }


    public void testGetOWLIndividuals() throws AlreadyImportedException {
        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
        OWLNamedClass parentClass = owlModel.createOWLNamedSubclass("Parent", personClass);
        OWLIndividual personA = personClass.createOWLIndividual("A");
        OWLIndividual personB = personClass.createOWLIndividual("B");
        OWLIndividual parentC = parentClass.createOWLIndividual("C");
        final Set indis = new HashSet(owlModel.getOWLIndividuals());
        assertSize(3, indis);
        assertContains(personA, indis);
        assertContains(personB, indis);
        assertContains(parentC, indis);
        OWLAllDifferent allDifferent = owlModel.createOWLAllDifferent();
        allDifferent.addDistinctMember(personA);
        allDifferent.addDistinctMember(personB);
        assertEquals(indis, new HashSet(owlModel.getOWLIndividuals()));
        owlModel.createOWLOntology("Test", "http://dummy/ont");
        assertEquals(indis, new HashSet(owlModel.getOWLIndividuals()));
    }


    public void testRDFIndividual() {
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        RDFIndividual indi = cls.createRDFIndividual("Individual");
        assertSize(0, owlModel.getOWLIndividuals());
    }


    public void testMatthewsOWLIndividuals() {
        // Create a class and an individual that is a memeber of it
        // (The individual should also be a member of owl:Thing)
        OWLNamedClass clsA = owlModel.createOWLNamedClass("ClsA");
        OWLIndividual indOfA = clsA.createOWLIndividual("indInstA");
        // Create and anonymous individual that is a member of
        // owl:Thing
        OWLIndividual anonInd = owlModel.getOWLThingClass().createOWLIndividual(owlModel.getNextAnonymousResourceName());
        // Create a someValuesFrom restriction and an individual  that is
        // a member of it.  This individual should also be a member of
        // owl:Thing
        OWLSomeValuesFrom someValuesFrom = owlModel.createOWLSomeValuesFrom(owlModel.createOWLObjectProperty("propP"),
                owlModel.createOWLNamedClass("ClsB"));
        OWLIndividual indOfAnon = (OWLIndividual) someValuesFrom.createInstance("indInstAnon");

        // All instances of owl:Thing
        Collection instancesOfOWLThing = owlModel.getOWLThingClass().getInstances(true);
        // OWLIndividuals (this should contain all individuals that are
        // members of owl:Thing
        Collection owlIndividuals = owlModel.getOWLIndividuals();

        // Check that all of the individuals that we created are
        // indeed instances of OWLIndividual
        assertTrue(indOfA instanceof OWLIndividual);
        assertTrue(anonInd instanceof OWLIndividual);
        assertTrue(indOfAnon instanceof OWLIndividual);

        // Now check that they are instances of owl:Thing
        // and that they are all returned in the call
        // to getOWLIndividuals.
        assertTrue(instancesOfOWLThing.contains(indOfA));
        assertTrue(owlIndividuals.contains(indOfA));
        assertTrue(instancesOfOWLThing.contains(anonInd));
        assertTrue(owlIndividuals.contains(anonInd));
        assertTrue(instancesOfOWLThing.contains(indOfAnon));
        assertTrue(owlIndividuals.contains(indOfAnon));
    }
}
