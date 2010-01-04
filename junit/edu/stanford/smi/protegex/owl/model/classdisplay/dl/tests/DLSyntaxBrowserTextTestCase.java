package edu.stanford.smi.protegex.owl.model.classdisplay.dl.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLUnionClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.classdisplay.dl.DLSyntaxBrowserTextGenerator;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 25, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DLSyntaxBrowserTextTestCase extends AbstractJenaTestCase {

    private static final String CLS_A = "ClsA";

    private static final String CLS_B = "ClsB";

    private static final String PROP_P = "propP";

    private static final String IND_A = "indA";

    private static final String IND_B = "indB";

    private OWLNamedClass clsA;

    private OWLNamedClass clsB;

    private OWLObjectProperty propP;

    private OWLIndividual indA;

    private OWLIndividual indB;

    private DLSyntaxBrowserTextGenerator gen;

    protected void setUp() throws Exception {
        super.setUp();
        gen = new DLSyntaxBrowserTextGenerator();
        createPrimitives();
    }

    private void createPrimitives() {
        clsA = owlModel.createOWLNamedClass(CLS_A);
        clsB = owlModel.createOWLNamedClass(CLS_B);
        propP = owlModel.createOWLObjectProperty(PROP_P);
        indA = owlModel.getOWLThingClass().createOWLIndividual(IND_A);
        indB = owlModel.getOWLThingClass().createOWLIndividual(IND_B);
    }

    public void testOWLAllValuesFrom() {
        OWLAllValuesFrom allValuesFrom = owlModel.createOWLAllValuesFrom(propP, clsA);
        allValuesFrom.accept(gen);
        assertEquals(allValuesFrom.getOperator() + " " + PROP_P + " " + CLS_A, gen.getBrowserText());
    }

    public void testNestedOWLAllValuesFrom() {
        OWLUnionClass unionClass = owlModel.createOWLUnionClass();
        unionClass.addOperand(clsA);
        unionClass.addOperand(clsB);
        OWLAllValuesFrom allValuesFrom = owlModel.createOWLAllValuesFrom(propP, unionClass);
        allValuesFrom.accept(gen);
        assertEquals(allValuesFrom.getOperator() + " " + PROP_P + " (" + CLS_A + " " + DefaultOWLUnionClass.OPERATOR + " " + CLS_B + ")", gen.getBrowserText());
    }

    public void testOWLIntersectionClass() {
        OWLIntersectionClass intersectionClass = owlModel.createOWLIntersectionClass();
        intersectionClass.addOperand(clsA);
        intersectionClass.addOperand(clsB);
        intersectionClass.accept(gen);
        assertEquals(CLS_A + " " + DefaultOWLIntersectionClass.OPERATOR + " " + CLS_B,
                gen.getBrowserText());
    }

    public void testEmptyObjects() {
        owlModel.createOWLAllValuesFrom().accept(gen);
        owlModel.createOWLSomeValuesFrom().accept(gen);
        owlModel.createOWLHasValue().accept(gen);
        owlModel.createOWLMinCardinality().accept(gen);
        owlModel.createOWLCardinality().accept(gen);
        owlModel.createOWLMinCardinality().accept(gen);
        owlModel.createOWLMaxCardinality().accept(gen);
        owlModel.createOWLIntersectionClass().accept(gen);
        owlModel.createOWLUnionClass().accept(gen);
        owlModel.createOWLComplementClass().accept(gen);
    }

}
