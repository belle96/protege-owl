package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class EquivalentClassTestCase extends AbstractJenaTestCase {

    /**
     * In response to a bug reported by Matthew Horridge:
     * 1. Create a class Animal
     * 2. Create a subclass of Animal called Herbivore
     * 3. Create a disjoint class to Animal called Plant
     * 4. Create a property call eats
     * 5. Add a definition to Herbivore in the following way:
     * i) Add Animal to the definition of Herbivore
     * ii) Now add a restriciton (* eats Plant) to the definition.
     * When the restriction is added, the Herbivore class completely disappears
     * from the class hierarchy pane.
     */
    public void testOnlyAnonymousEquivalentClass() {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass herbivoreCls = owlModel.createOWLNamedClass("Herbivore");
        OWLNamedClass plantCls = owlModel.createOWLNamedClass("Plant");
        herbivoreCls.addSuperclass(animalCls);
        herbivoreCls.removeSuperclass(owlThing);
        OWLObjectProperty eatsProperty = owlModel.createOWLObjectProperty("eats");
        herbivoreCls.removeSuperclass(animalCls);
        herbivoreCls.addEquivalentClass(animalCls);
        OWLAllValuesFrom restriction = owlModel.createOWLAllValuesFrom(eatsProperty, plantCls);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(animalCls);
        intersectionCls.addOperand(restriction);
        herbivoreCls.setDefinition(intersectionCls);
        assertEquals(2, herbivoreCls.getSuperclassCount());
        assertTrue(herbivoreCls.getSuperclasses(false).contains(animalCls));
    }


    public void testOWLEquivalentClassTriple1() {
        final RDFProperty property = owlModel.getOWLEquivalentClassProperty();
        OWLNamedClass c = owlModel.createOWLNamedClass("C");
        RDFSClass other = owlModel.createOWLComplementClass(c);
        c.addSuperclass(other);
        assertSize(0, c.getPropertyValues(property));
        other.addSuperclass(c);
        assertSize(1, c.getPropertyValues(property));
        assertEquals(other, c.getPropertyValue(property));
    }


    public void testOWLEquivalentClassTriple2() {
        final RDFProperty property = owlModel.getOWLEquivalentClassProperty();
        OWLNamedClass c = owlModel.createOWLNamedClass("C");
        RDFSClass other = owlModel.createOWLComplementClass(c);
        other.addSuperclass(c);
        assertSize(0, c.getPropertyValues(property));
        c.addSuperclass(other);
        assertSize(1, c.getPropertyValues(property));
        assertEquals(other, c.getPropertyValue(property));
    }
}
