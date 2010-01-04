package edu.stanford.smi.protegex.owl.ui.existential.tests;

import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.existential.Existential;

import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExistentialTestCase extends AbstractJenaTestCase {

    public void test1DirectSuperclasses() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("hasPart");
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Host");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");
        hostCls.addSuperclass(bCls);
        hostCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, aCls));
        List ts = Existential.getExistentialDependents(hostCls, property);
        assertSize(1, ts);
        assertEquals(aCls, ts.get(0));
    }


    public void test2DirectSuperclasses() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("hasPart");
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Host");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");
        hostCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, bCls));
        hostCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, aCls));
        List ts = Existential.getExistentialDependents(hostCls, property);
        assertSize(2, ts);
        assertEquals(aCls, ts.get(0));
        assertEquals(bCls, ts.get(1));
    }


    public void testAncestorClasses() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("hasPart");
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Host");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");
        aCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, bCls));
        hostCls.addSuperclass(aCls);
        List ts = Existential.getExistentialDependents(hostCls, property);
        assertSize(1, ts);
        assertEquals(bCls, ts.get(0));

    }


    public void test1EquivalentClass() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("hasPart");
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Host");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(bCls);
        intersectionCls.addOperand(owlModel.createOWLSomeValuesFrom(property, aCls));
        hostCls.addEquivalentClass(intersectionCls);
        List ts = Existential.getExistentialDependents(hostCls, property);
        assertSize(1, ts);
        assertEquals(aCls, ts.get(0));
    }
}
