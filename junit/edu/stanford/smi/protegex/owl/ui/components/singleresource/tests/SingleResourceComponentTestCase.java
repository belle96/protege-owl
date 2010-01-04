package edu.stanford.smi.protegex.owl.ui.components.singleresource.tests;

import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.components.singleresource.SingleResourceComponent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SingleResourceComponentTestCase extends AbstractJenaTestCase {

    public void testActionsEnabled() {
        OWLNamedClass type = owlModel.createOWLNamedClass("Class");
        RDFProperty predicate = owlModel.createOWLObjectProperty("property");
        OWLIndividual individual = type.createOWLIndividual("Individual");
        SingleResourceComponent c = new SingleResourceComponent(predicate);
        c.setSubject(individual);
        assertTrue(c.isSetEnabled());
        assertTrue(c.isCreateEnabled());
        assertFalse(c.isRemoveEnabled());
        individual.setPropertyValue(predicate, owlThing);
        c.valuesChanged();
        assertTrue(c.isSetEnabled());
        assertTrue(c.isCreateEnabled());
        assertTrue(c.isRemoveEnabled());
    }


    public void testCreateButtonDisabledForEnumeratedClass() {
        OWLNamedClass type = owlModel.createOWLNamedClass("Class");
        RDFProperty predicate = owlModel.createOWLObjectProperty("property");
        OWLNamedClass enumClass = owlModel.createOWLNamedClass("Enum");
        final OWLEnumeratedClass enumeration = owlModel.createOWLEnumeratedClass();
        enumeration.addOneOf(owlThing);
        enumeration.addOneOf(owlModel.getOWLNamedClassClass());
        predicate.setRange(enumClass);
        OWLIndividual individual = type.createOWLIndividual("Individual");
        SingleResourceComponent c = new SingleResourceComponent(predicate);
        c.setSubject(individual);
        assertTrue(c.isCreateEnabled());
        enumClass.setDefinition(enumeration);
        assertFalse(c.isCreateEnabled());
    }


    public void testHasValueRestrictions() {
        OWLNamedClass type = owlModel.createOWLNamedClass("Class");
        RDFProperty predicate = owlModel.createOWLObjectProperty("property");
        OWLIndividual individual = type.createOWLIndividual("Individual");
        type.addSuperclass(owlModel.createOWLHasValue(predicate, owlThing));
        SingleResourceComponent c = new SingleResourceComponent(predicate);
        c.setSubject(individual);
        c.valuesChanged();
        assertFalse(c.isSetEnabled());
        assertFalse(c.isCreateEnabled());
        assertFalse(c.isRemoveEnabled());
        assertEquals(owlThing, c.getResource());
    }


    public void testSubpropertyValue() {
        OWLNamedClass type = owlModel.createOWLNamedClass("Class");
        RDFProperty predicate = owlModel.createOWLObjectProperty("superproperty");
        RDFProperty subproperty = owlModel.createOWLObjectProperty("subproperty");
        subproperty.addSuperproperty(predicate);
        OWLIndividual subject = type.createOWLIndividual("Individual");
        subject.addPropertyValue(subproperty, owlThing);
        SingleResourceComponent c = new SingleResourceComponent(predicate);
        c.setSubject(subject);
        c.valuesChanged();
        assertEquals(owlThing, c.getResource());
        assertTrue(c.isSetEnabled());
        assertTrue(c.isCreateEnabled());
        assertFalse(c.isRemoveEnabled());
    }
}
