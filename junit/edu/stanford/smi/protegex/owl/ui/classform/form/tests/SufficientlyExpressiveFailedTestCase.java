package edu.stanford.smi.protegex.owl.ui.classform.form.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.classform.form.ClassFormSwitchableType;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SufficientlyExpressiveFailedTestCase extends AbstractJenaTestCase {

    public void testRDFSNamedClass() {
        RDFSNamedClass namedClass = owlModel.createRDFSNamedClass("Class");
        assertFalse(new ClassFormSwitchableType().isSufficientlyExpressive(namedClass));
    }


    public void testOWLNamedClassWithoutRestrictions() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        assertTrue(new ClassFormSwitchableType().isSufficientlyExpressive(namedClass));
    }


    public void testSuperclassOWLSomeValuesFrom() {
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        namedClass.addSuperclass(owlModel.createOWLSomeValuesFrom(property, namedClass));
        assertTrue(new ClassFormSwitchableType().isSufficientlyExpressive(namedClass));
    }


    public void testSuperclassOWLAllValuesFromOnly() {
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        namedClass.addSuperclass(owlModel.createOWLAllValuesFrom(property, namedClass));
        assertFalse(new ClassFormSwitchableType().isSufficientlyExpressive(namedClass));
    }


    public void testSuperclassClosureAxiom() {
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        RDFSClass filler = namedClass;
        namedClass.addSuperclass(owlModel.createOWLAllValuesFrom(property, filler));
        namedClass.addSuperclass(owlModel.createOWLSomeValuesFrom(property, filler));
        assertTrue(new ClassFormSwitchableType().isSufficientlyExpressive(namedClass));
    }


    public void testSuperclassNonClosureAxiom() {
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        RDFSClass filler = namedClass;
        RDFSClass otherFiller = owlModel.createOWLNamedClass("Other");
        assertFalse(filler.equals(otherFiller));
        namedClass.addSuperclass(owlModel.createOWLAllValuesFrom(property, filler));
        namedClass.addSuperclass(owlModel.createOWLSomeValuesFrom(property, filler));
        namedClass.addSuperclass(owlModel.createOWLSomeValuesFrom(property, otherFiller));
        assertFalse(new ClassFormSwitchableType().isSufficientlyExpressive(namedClass));
    }
}
