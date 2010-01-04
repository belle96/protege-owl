package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.ArrayList;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 4, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class EqualsStructurallyTestCase extends AbstractJenaTestCase {

    protected void setUp()
            throws Exception {
        super.setUp();
        owlModel.createOWLNamedClass("A");
        owlModel.createOWLNamedClass("B");
        owlModel.createOWLNamedClass("C");
        owlModel.createOWLObjectProperty("p");
        owlModel.createOWLDatatypeProperty("r");
        owlModel.createOWLDatatypeProperty("s");
        owlModel.getOWLThingClass().createOWLIndividual("i");
        owlModel.getOWLThingClass().createOWLIndividual("j");
    }


    public void testOWLNamedClass() {
        assertTrue(owlModel.getOWLNamedClass("A").equalsStructurally(owlModel.getOWLNamedClass("A")));
        assertFalse(owlModel.getOWLNamedClass("A").equalsStructurally(owlModel.getOWLNamedClass("B")));
    }


    public void testOWLObjectProperty() {
        assertTrue(owlModel.getOWLObjectProperty("p").equalsStructurally(owlModel.getOWLObjectProperty("p")));
        assertFalse(owlModel.getOWLObjectProperty("p").equalsStructurally(owlModel.getOWLObjectProperty("q")));
    }


    public void testOWLSomeValuesFrom() {
        RDFResource resA = owlModel.createOWLSomeValuesFrom(getP(), getA());
        RDFResource resB = owlModel.createOWLSomeValuesFrom(getP(), getA());
        RDFResource resC = owlModel.createOWLSomeValuesFrom(getP(), getB());
        assertTrue(resA.equalsStructurally(resB));
        assertFalse(resA.equalsStructurally(resC));
    }


    public void testOWLAllValuesFrom() {
        RDFResource resA = owlModel.createOWLAllValuesFrom(getP(), getA());
        RDFResource resB = owlModel.createOWLAllValuesFrom(getP(), getA());
        RDFResource resC = owlModel.createOWLAllValuesFrom(getP(), getB());
        assertTrue(resA.equalsStructurally(resB));
        assertFalse(resA.equalsStructurally(resC));
    }


    public void testAbstractCardinality() {
        RDFResource resA = owlModel.createOWLMinCardinality(getP(), 1);
        RDFResource resB = owlModel.createOWLMinCardinality(getP(), 1);
        RDFResource resC = owlModel.createOWLMinCardinality(getP(), 2);
        assertTrue(resA.equalsStructurally(resB));
        assertFalse(resA.equalsStructurally(resC));
    }


    public void testOWLAbstractQCardinality() {
        OWLMinCardinality resA = owlModel.createOWLMinCardinality(getP(), 1);
        resA.setValuesFrom(getA());
        OWLMinCardinality resB = owlModel.createOWLMinCardinality(getP(), 1);
        resB.setValuesFrom(getA());
        OWLMinCardinality resC = owlModel.createOWLMinCardinality(getP(), 1);
        resC.setValuesFrom(getB());
        assertTrue(resA.equalsStructurally(resB));
        assertFalse(resA.equalsStructurally(resC));
    }


    public void testObjectOWLHasValue() {
        OWLHasValue resA = owlModel.createOWLHasValue(getP(), getI());
        OWLHasValue resB = owlModel.createOWLHasValue(getP(), getI());
        OWLHasValue resC = owlModel.createOWLHasValue(getP(), getJ());
        assertTrue(resA.equalsStructurally(resB));
        assertFalse(resA.equalsStructurally(resC));
    }


    public void testDatatypeOWLHasValue() {
        OWLHasValue resA = owlModel.createOWLHasValue(getR(), owlModel.createRDFSLiteral("3", owlModel.getXSDint()));
        OWLHasValue resB = owlModel.createOWLHasValue(getR(), owlModel.createRDFSLiteral("3", owlModel.getXSDint()));
        OWLHasValue resC = owlModel.createOWLHasValue(getR(), owlModel.createRDFSLiteral("4", owlModel.getXSDint()));
        assertTrue(resA.equalsStructurally(resB));
        assertFalse(resA.equalsStructurally(resC));
    }


    public void testAbstractLogicalNArayClass() {
        OWLIntersectionClass resA = owlModel.createOWLIntersectionClass();
        resA.addOperand(getA());
        resA.addOperand(getB());
        OWLIntersectionClass resB = owlModel.createOWLIntersectionClass();
        resB.addOperand(getB());
        resB.addOperand(getA());
        OWLIntersectionClass resC = owlModel.createOWLIntersectionClass();
        resC.addOperand(getA());
        resC.addOperand(getB());
        resC.addOperand(getC());
        assertTrue(resA.equalsStructurally(resB));
        assertFalse(resA.equalsStructurally(resC));
    }


    public void testOWLEnumeratedClass() {
        OWLEnumeratedClass resA = owlModel.createOWLEnumeratedClass();
        resA.addOneOf(getI());
        resA.addOneOf(getJ());
        OWLEnumeratedClass resB = owlModel.createOWLEnumeratedClass();
        resB.addOneOf(getJ());
        resB.addOneOf(getI());
        OWLEnumeratedClass resC = owlModel.createOWLEnumeratedClass();
        resC.addOneOf(getI());
        assertTrue(resA.equalsStructurally(resB));
        assertFalse(resA.equalsStructurally(resC));
    }


    public void testRDFList() {
        ArrayList valuesA = new ArrayList();
        valuesA.add(getA());
        valuesA.add(getB());
        valuesA.add(getC());
        ArrayList valuesB = new ArrayList();
        valuesB.add(getC());
        valuesB.add(getB());
        valuesB.add(getA());
        RDFList resA = owlModel.createRDFList(valuesA.iterator());
        RDFList resB = owlModel.createRDFList(valuesA.iterator());
        RDFList resC = owlModel.createRDFList(valuesB.iterator());
        assertTrue(resA.equalsStructurally(resB));
        assertFalse(resA.equalsStructurally(resC));
    }


    public void testOWLComplementClass() {
        RDFResource resA = owlModel.createOWLComplementClass(getA());
        RDFResource resB = owlModel.createOWLComplementClass(getA());
        RDFResource resC = owlModel.createOWLComplementClass(getB());
        assertTrue(resA.equalsStructurally(resB));
        assertFalse(resA.equalsStructurally(resC));
    }


    private OWLObjectProperty getP() {
        return owlModel.getOWLObjectProperty("p");
    }


    private OWLDatatypeProperty getR() {
        return owlModel.getOWLDatatypeProperty("r");
    }


    private OWLNamedClass getA() {
        return owlModel.getOWLNamedClass("A");
    }


    private OWLNamedClass getB() {
        return owlModel.getOWLNamedClass("B");
    }


    private OWLNamedClass getC() {
        return owlModel.getOWLNamedClass("C");
    }


    private OWLIndividual getI() {
        return owlModel.getOWLIndividual("i");
    }


    private OWLIndividual getJ() {
        return owlModel.getOWLIndividual("j");
    }
}

