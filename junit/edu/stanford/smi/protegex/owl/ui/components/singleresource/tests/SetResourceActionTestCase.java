package edu.stanford.smi.protegex.owl.ui.components.singleresource.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.components.singleresource.SetResourceAction;
import edu.stanford.smi.protegex.owl.ui.components.singleresource.SingleResourceComponent;

import java.util.Collections;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SetResourceActionTestCase extends AbstractJenaTestCase {

    public void testRDFSRange() {
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        RDFProperty property = owlModel.createRDFProperty("property");
        property.setRange(cls);
        SingleResourceComponent component = new SingleResourceComponent(property);
        component.setSubject(owlThing);
        SetResourceAction action = new SetResourceAction(component);
        assertSize(0, action.getSelectableResources());
        RDFIndividual individual = cls.createRDFIndividual("individual");
        assertSize(1, action.getSelectableResources());
        assertContains(individual, action.getSelectableResources());
    }


    public void testOWLAllValuesFrom() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLIndividual individual = cls.createOWLIndividual("individual");
        cls.createOWLIndividual("Other");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass rangeClass = owlModel.createOWLNamedClass("Color");
        cls.addSuperclass(owlModel.createOWLAllValuesFrom(property, rangeClass));
        OWLIndividual rangeIndividualA = rangeClass.createOWLIndividual("A");
        OWLIndividual rangeIndividualB = rangeClass.createOWLIndividual("B");
        SingleResourceComponent component = new SingleResourceComponent(property);
        component.setSubject(individual);
        SetResourceAction action = new SetResourceAction(component);
        assertSize(2, action.getSelectableResources());
        assertContains(rangeIndividualA, action.getSelectableResources());
        assertContains(rangeIndividualB, action.getSelectableResources());
    }


    public void testOWLAllValuesFromEnumeratedClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLIndividual individual = cls.createOWLIndividual("individual");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass rangeClass = owlModel.createOWLNamedClass("Color");
        OWLIndividual rangeIndividualA = rangeClass.createOWLIndividual("A");
        OWLIndividual rangeIndividualB = rangeClass.createOWLIndividual("B");
        OWLEnumeratedClass enumeratedClass = owlModel.createOWLEnumeratedClass(Collections.singleton(rangeIndividualA));
        cls.addSuperclass(owlModel.createOWLAllValuesFrom(property, enumeratedClass));
        SingleResourceComponent component = new SingleResourceComponent(property);
        component.setSubject(individual);
        SetResourceAction action = new SetResourceAction(component);
        assertSize(1, action.getSelectableResources());
        assertContains(rangeIndividualA, action.getSelectableResources());
    }
}
