package edu.stanford.smi.protegex.owl.model.classparser.dl.tests;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLAllValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLCardinality;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLMaxCardinality;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.model.classparser.dl.DLSyntaxParser;
import edu.stanford.smi.protegex.owl.model.classparser.dl.ParseException;
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
public class DLSyntaxParserTestCase extends AbstractJenaTestCase {


    protected void setUp() throws Exception {
        super.setUp();
        owlModel.createOWLNamedClass("A");
        owlModel.createOWLNamedClass("B");
        owlModel.createOWLNamedClass("C");
        owlModel.createOWLObjectProperty("p");
        owlModel.createOWLDatatypeProperty("q");
        owlModel.getOWLThingClass().createOWLIndividual("i");
        owlModel.getOWLThingClass().createOWLIndividual("j");
        owlModel.getOWLThingClass().createOWLIndividual("k");
    }

    private void parsePass(String expression, Class c) {
        try {
            OWLClass cls = DLSyntaxParser.parseExpression(owlModel, expression, true);
            Log.getLogger().info("Class: " + cls.getBrowserText());
            assertTrue(c.isInstance(cls));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    public void testOWLUnionClass() {
        parsePass("A | B | C", OWLUnionClass.class);
    }

    public void testOWLIntersectionClass() {
        parsePass("A & B & C", OWLIntersectionClass.class);
    }

    public void testOWLAllValuesFromDatatype() {
        parsePass("* q int", OWLAllValuesFrom.class);
    }

    public void testOWLAllValuesFrom() {
        parsePass("* p A", OWLAllValuesFrom.class);
    }

    public void testOWLSomeValuesFrom() {
        parsePass("? p A", OWLSomeValuesFrom.class);
    }

    public void testOWLMinCardinality() {
        parsePass("> 3 p", OWLMinCardinality.class);
    }

    public void testOWLMinQCardinality() {
        parsePass("> 3 p A", OWLMinCardinality.class);
    }

    public void testOWLCardinality() {
        parsePass("= 3 p", OWLCardinality.class);
    }

    public void testOWLQCardinality() {
        parsePass("= 3 p A", OWLCardinality.class);
    }

    public void testOWLMaxCardinality() {
        parsePass("< 3 p", OWLMaxCardinality.class);
    }

    public void testOWLMaxQCardinality() {
        parsePass("< 3 p A", OWLMaxCardinality.class);
    }

    public void testOWLEnumerationClass() {
        parsePass("{i j k}", OWLEnumeratedClass.class);
    }

    public void testOWLComplementClass() {
        parsePass("!A", OWLComplementClass.class);
    }

    public void testComplexFiller() {
        parsePass("? p (! (A | B))", OWLSomeValuesFrom.class);
    }






}
