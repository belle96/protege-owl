package edu.stanford.smi.protegex.owl.ui.classform.component.property.tests;

import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.classform.component.property.PropertyFormTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyFormTableModelTestCase extends AbstractJenaTestCase {

    public void testEmptyOnNoRestriction() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        PropertyFormTableModel tableModel = new PropertyFormTableModel(namedClass, property);
        assertEquals(0, tableModel.getRowCount());
    }


    public void testOneHasValueSuperclasses() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        RDFResource filler = owlThing.createOWLIndividual("Individual");
        namedClass.addSuperclass(owlModel.createOWLHasValue(property, filler));
        PropertyFormTableModel tableModel = new PropertyFormTableModel(namedClass, property);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(filler, tableModel.getRDFResource(0));
    }


    public void testOneSomeValuesFromSuperclasses() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        RDFSClass filler = owlThing;
        namedClass.addSuperclass(owlModel.createOWLSomeValuesFrom(property, filler));
        PropertyFormTableModel tableModel = new PropertyFormTableModel(namedClass, property);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(filler, tableModel.getRDFResource(0));
    }


    public void testTwoSomeValuesFromSuperclasses() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        RDFSClass fillerA = owlModel.createOWLNamedClass("A");
        RDFSClass fillerB = owlModel.createOWLNamedClass("B");
        namedClass.addSuperclass(owlModel.createOWLSomeValuesFrom(property, fillerB));
        namedClass.addSuperclass(owlModel.createOWLSomeValuesFrom(property, fillerA));
        PropertyFormTableModel tableModel = new PropertyFormTableModel(namedClass, property);
        assertEquals(2, tableModel.getRowCount());
        assertEquals(fillerA, tableModel.getRDFResource(0));
        assertEquals(fillerB, tableModel.getRDFResource(1));
    }


    public void testOneSomeValuesFromAsEquivalentClass() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        RDFSClass filler = owlThing;
        namedClass.setDefinition(owlModel.createOWLSomeValuesFrom(property, filler));
        PropertyFormTableModel tableModel = new PropertyFormTableModel(namedClass, property);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(filler, tableModel.getRDFResource(0));
    }


    public void testOneSomeValuesFromAsPartOfEquivalentClass() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        OWLIntersectionClass definition = owlModel.createOWLIntersectionClass();
        definition.addOperand(owlThing);
        RDFSClass filler = owlThing;
        definition.addOperand(owlModel.createOWLSomeValuesFrom(property, filler));
        namedClass.setDefinition(definition);
        PropertyFormTableModel tableModel = new PropertyFormTableModel(namedClass, property);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(filler, tableModel.getRDFResource(0));
    }
}
