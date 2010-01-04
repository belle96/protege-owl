package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultRDFIndividualTestCase extends AbstractJenaTestCase {


    public void testGetRDFProperties() {
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        RDFIndividual individual = cls.createRDFIndividual("Individual");
        assertSize(1, individual.getRDFProperties());
        assertContains(owlModel.getRDFTypeProperty(), individual.getRDFProperties());
    }


    public void testGetHasValuesOnTypes() {
        OWLNamedClass superclass = owlModel.createOWLNamedClass("Superclass");
        OWLNamedClass subclass = owlModel.createOWLNamedSubclass("Subclass", superclass);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        RDFResource individual = subclass.createInstance("Instance");
        assertSize(0, individual.getHasValuesOnTypes(property));
        superclass.addSuperclass(owlModel.createOWLHasValue(property, owlThing));
        assertSize(1, individual.getHasValuesOnTypes(property));
        assertContains(owlThing, individual.getHasValuesOnTypes(property));
    }


    public void testPropertyValue() {
        RDFSNamedClass c = owlModel.createRDFSNamedClass("Person");
        RDFProperty childrenProperty = owlModel.createRDFProperty("children");
        RDFProperty sonsProperty = owlModel.createRDFProperty("sons");
        sonsProperty.addSuperproperty(childrenProperty);
        RDFIndividual individual = (RDFIndividual) c.createInstance("instance");
        RDFIndividual daughter = (RDFIndividual) c.createInstance("daughter");
        RDFIndividual son = (RDFIndividual) c.createInstance("son");
        individual.addPropertyValue(childrenProperty, daughter);
        individual.addPropertyValue(sonsProperty, son);
        assertEquals(1, individual.getPropertyValueCount(childrenProperty));
        assertEquals(1, individual.getPropertyValueCount(sonsProperty));
        assertSize(1, individual.getPropertyValues(childrenProperty, false));
        assertEquals(daughter, individual.getPropertyValue(childrenProperty, false));
        assertSize(1, individual.getPropertyValues(sonsProperty, false));
        assertEquals(son, individual.getPropertyValue(sonsProperty, false));
        assertSize(2, individual.getPropertyValues(childrenProperty, true));
        assertContains(son, individual.getPropertyValues(childrenProperty, true));
        assertContains(daughter, individual.getPropertyValues(childrenProperty, true));
    }
}
