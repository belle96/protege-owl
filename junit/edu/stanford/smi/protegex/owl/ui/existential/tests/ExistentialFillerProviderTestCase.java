package edu.stanford.smi.protegex.owl.ui.existential.tests;

import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.ui.existential.ExistentialFillerProvider;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Feb 5, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ExistentialFillerProviderTestCase extends AbstractJenaTestCase {

    public void testNamedSuperClasses() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("A");
        OWLObjectProperty prop = owlModel.createOWLObjectProperty("p");
        OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
        OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
        cls.addSuperclass(owlModel.createOWLSomeValuesFrom(prop, clsB));
        cls.addSuperclass(owlModel.createOWLSomeValuesFrom(prop, clsC));
        ExistentialFillerProvider prov = new ExistentialFillerProvider(prop);
        cls.accept(prov);
        assertSize(2, prov.getFillers());
        assertContains(clsB, prov.getFillers());
        assertContains(clsC, prov.getFillers());
    }

    public void testNamedEquivalentClasses() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("A");
        OWLObjectProperty prop = owlModel.createOWLObjectProperty("p");
        OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
        OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
        cls.addEquivalentClass(owlModel.createOWLSomeValuesFrom(prop, clsB));
        cls.addSuperclass(owlModel.createOWLSomeValuesFrom(prop, clsC));
        ExistentialFillerProvider prov = new ExistentialFillerProvider(prop);
        cls.accept(prov);
        assertSize(2, prov.getFillers());
        assertContains(clsB, prov.getFillers());
        assertContains(clsC, prov.getFillers());
    }

    public void testNonTransitiveChain() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("A");
        OWLObjectProperty prop = owlModel.createOWLObjectProperty("p");
        OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
        OWLSomeValuesFrom nestedSomeValuesFrom = owlModel.createOWLSomeValuesFrom(prop, clsC);
        cls.addSuperclass(owlModel.createOWLSomeValuesFrom(prop, nestedSomeValuesFrom));
        ExistentialFillerProvider prov = new ExistentialFillerProvider(prop);
        cls.accept(prov);
        assertSize(1, prov.getFillers());
        assertContains(nestedSomeValuesFrom, prov.getFillers());
    }

    public void testTransitiveChain() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("A");
        OWLObjectProperty prop = owlModel.createOWLObjectProperty("p");
        prop.setTransitive(true);
        OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
        OWLSomeValuesFrom nestedSomeValuesFrom = owlModel.createOWLSomeValuesFrom(prop, clsC);
        cls.addSuperclass(owlModel.createOWLSomeValuesFrom(prop, nestedSomeValuesFrom));
        ExistentialFillerProvider prov = new ExistentialFillerProvider(prop);
        cls.accept(prov);
        assertSize(2, prov.getFillers());
        assertContains(clsC, prov.getFillers());
    }

    public void testIntersectionEquivalentClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("A");
        OWLObjectProperty prop = owlModel.createOWLObjectProperty("p");
        OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
        OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
        OWLIntersectionClass intersectionClass = owlModel.createOWLIntersectionClass();
        OWLSomeValuesFrom someValuesFromB = owlModel.createOWLSomeValuesFrom(prop, clsB);
        OWLSomeValuesFrom someValuesFromC = owlModel.createOWLSomeValuesFrom(prop, clsC);
        intersectionClass.addOperand(someValuesFromB);
        intersectionClass.addOperand(someValuesFromC);
        cls.addEquivalentClass(intersectionClass);
        ExistentialFillerProvider prov = new ExistentialFillerProvider(prop);
        cls.accept(prov);
        assertSize(2, prov.getFillers());
        assertContains(clsB, prov.getFillers());
        assertContains(clsC, prov.getFillers());
    }

    public void testFillerCycle() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("A");
        OWLObjectProperty prop = owlModel.createOWLObjectProperty("p");
        cls.addSuperclass(owlModel.createOWLSomeValuesFrom(prop, cls));
        ExistentialFillerProvider prov = new ExistentialFillerProvider(prop);
        cls.accept(prov);
        assertSize(1, prov.getFillers());
        assertContains(cls, prov.getFillers());
    }

    public void testHierarchyCycle() {
        OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
        OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
        OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
        OWLObjectProperty prop = owlModel.createOWLObjectProperty("p");
        clsA.addSuperclass(owlModel.createOWLSomeValuesFrom(prop, clsC));
        clsA.addSuperclass(clsB);
        clsB.addSuperclass(clsA);
        ExistentialFillerProvider prov = new ExistentialFillerProvider(prop);
        clsA.accept(prov);
        assertSize(1, prov.getFillers());
        assertContains(clsC, prov.getFillers());
    }
}
