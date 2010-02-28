package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLAllDifferent;
import edu.stanford.smi.protegex.owl.model.OWLCardinality;
import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLDataRange;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLMaxCardinality;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.OWLNamespaceManager;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.writer.rdfxml.renderer.RDFResourceRenderer;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 8, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RDFXMLWriterTestCase extends AbstractRDFXMLWriterTestCases {

    private OWLNamedClass clsA;

    private OWLNamedClass clsB;

    private OWLNamedClass clsC;

    private OWLObjectProperty propP;

    private OWLDatatypeProperty propR;

    private OWLIndividual iA;

    private OWLIndividual iB;


    protected void setUp()
            throws Exception {
        Log.setLoggingLevel(AbstractRDFXMLWriterTestCases.class, Level.FINE);
        super.setUp();
        clsA = owlModel.createOWLNamedClass("A");
        clsB = owlModel.createOWLNamedClass("B");
        clsC = owlModel.createOWLNamedClass("C");
        propP = owlModel.createOWLObjectProperty("p");
        propR = owlModel.createOWLDatatypeProperty("r");
        iA = owlModel.getOWLThingClass().createOWLIndividual("iA");
        iB = owlModel.getOWLThingClass().createOWLIndividual("iB");
    }


    public void testAllDifferent() {
        // All different
        OWLAllDifferent allDifferent = owlModel.createOWLAllDifferent();
        allDifferent.addDistinctMember(iA);
        allDifferent.addDistinctMember(iB);
        doCheck();
    }


    public void testSubClassOf() {
        clsA.addSuperclass(clsB);
        clsA.addSuperclass(clsC);
        doCheck();
    }


    public void testDisjointWith() {
        clsA.addDisjointClass(clsB);
        doCheck();
    }


    public void testObjectSomeValuesFrom() {
        clsA.addSuperclass(owlModel.createOWLSomeValuesFrom(propP, clsB));
        doCheck();
    }


    public void testDataSomeValuesFrom() {
        clsA.addSuperclass(owlModel.createOWLSomeValuesFrom(propR, owlModel.getXSDint()));
        doCheck();
    }


    public void testObjectAllValuesFrom() {
        clsB.addSuperclass(owlModel.createOWLAllValuesFrom(propP, clsB));
        doCheck();
    }


    public void testDataAllValuesFrom() {
        clsA.addSuperclass(owlModel.createOWLAllValuesFrom(propR, owlModel.getXSDint()));
        doCheck();
    }


    public void testObjectHasValue() {
        clsA.addSuperclass(owlModel.createOWLHasValue(propP, iA));
        doCheck();
    }


    public void testDataHasValue() {
        clsA.addSuperclass(owlModel.createOWLHasValue(propR, owlModel.createRDFSLiteral("26", owlModel.getXSDint())));
        doCheck();
    }


    public void testIntersectionOf() {
        OWLIntersectionClass intersectionClass = owlModel.createOWLIntersectionClass();
        intersectionClass.addOperand(clsB);
        intersectionClass.addOperand(clsC);
        clsA.addSuperclass(intersectionClass);
        doCheck();
    }


    public void testUnionOf() {
        OWLUnionClass unionClass = owlModel.createOWLUnionClass();
        unionClass.addOperand(clsB);
        unionClass.addOperand(clsC);
        clsA.addSuperclass(unionClass);
        doCheck();
    }


    public void testComplementOf() {
        OWLComplementClass complementClass = owlModel.createOWLComplementClass();
        complementClass.setComplement(clsB);
        clsA.addSuperclass(complementClass);
        doCheck();
    }


    public void testOneOf() {
        OWLEnumeratedClass enumeratedClass = owlModel.createOWLEnumeratedClass();
        enumeratedClass.addOneOf(iA);
        enumeratedClass.addOneOf(iB);
        clsA.addSuperclass(enumeratedClass);
        doCheck();
    }

   
    public void testMinCardinality() {
        clsA.addSuperclass(owlModel.createOWLMinCardinality(propP, 3));
        RDFResourceRenderer.setRenderCardinalityAsInt(true);
        doCheck();
    }

    public void testCardinality() {
        clsA.addSuperclass(owlModel.createOWLCardinality(propP, 3));
        RDFResourceRenderer.setRenderCardinalityAsInt(true);
        doCheck();
    }


    public void testMaxCardinality() {
        clsA.addSuperclass(owlModel.createOWLMaxCardinality(propP, 3));
        RDFResourceRenderer.setRenderCardinalityAsInt(true);
        doCheck();
    }


    public void testMinQualifiedCardinality() {
        OWLMinCardinality cardinality = owlModel.createOWLMinCardinality(propP, 3);
        cardinality.setValuesFrom(clsA);
        clsA.addSuperclass(cardinality);
        RDFResourceRenderer.setRenderCardinalityAsInt(true);
        doCheck();
    }


    public void testQualifiedCardinality() {
        OWLCardinality cardinality = owlModel.createOWLCardinality(propP, 3);
        cardinality.setValuesFrom(clsA);
        clsA.addSuperclass(cardinality);
        RDFResourceRenderer.setRenderCardinalityAsInt(true);
        doCheck();
    }


    public void testMaxQualifiedCardinality() {
        OWLMaxCardinality cardinality = owlModel.createOWLMaxCardinality(propP, 3);
        cardinality.setValuesFrom(clsA);
        clsA.addSuperclass(cardinality);
        RDFResourceRenderer.setRenderCardinalityAsInt(true);
        doCheck();
    }


    public void testPropertyCharacteristics() {
        propP.setFunctional(true);
        propP.setInverseFunctional(true);
        propP.setSymmetric(true);
        propP.setTransitive(true);
        doCheck();
    }


    public void testObjectPropertyRange() {
        propP.setRange(clsA);
        doCheck();
    }


    public void testObjectPropertyDomain() {
        propP.setDomain(clsA);
        doCheck();
    }


    public void testDatatypePropertyRange() {
        propR.setRange(owlModel.getXSDint());
        doCheck();
    }


    public void testDatatypePropertyDataRange() {
        RDFSLiteral [] literals = new RDFSLiteral[3];
        literals[0] = owlModel.createRDFSLiteral("26", owlModel.getXSDint());
        literals[1] = owlModel.createRDFSLiteral("33", owlModel.getXSDint());
        literals[2] = owlModel.createRDFSLiteral("44", owlModel.getXSDint());
        OWLDataRange dataRange = owlModel.createOWLDataRange(literals);
        propR.setRange(dataRange);
        doCheck();
    }

//	public void testInverseProperty() {
//		propP.setInverseProperty(owlModel.createOWLObjectProperty("propQ"));
//		doCheck();
//	}


    public void testRDFList() {
        ArrayList list = new ArrayList();
        list.add(iA);
        list.add(iB);
        list.add(clsA);
        RDFList rdfList = owlModel.createRDFList(list.iterator());
        iA.addPropertyValue(propP, rdfList);
        doCheck();
    }


    public void testSameAs() {
        iA.addSameAs(iB);
        doCheck();
    }


    public void testDifferentFrom() {
        iA.addDifferentFrom(iB);
        doCheck();
    }


    public void testAnnotationProperty() {
        OWLProperty prop = owlModel.createAnnotationOWLObjectProperty("anno");
        doCheck();
    }


    public void testBuiltInAnnotation() {
        OWLUtil.addComment(clsA, "A comment");
        doCheck();
    }


    public void testBuiltInAnonymousClassAnnotation() {
        OWLRestriction restriction = owlModel.createOWLSomeValuesFrom(propP, clsB);
        OWLUtil.addComment(restriction, "A comment");
        clsA.addSuperclass(restriction);
        doCheck();
    }


    public void testAnonymousIndividualChain() {
        owlModel.setExpandShortNameInMethods(false);
        RDFResource anonA = clsA.createOWLIndividual(owlModel.getNextAnonymousResourceName());
        RDFResource anonB = clsA.createOWLIndividual(owlModel.getNextAnonymousResourceName());
        RDFResource anonC = clsA.createOWLIndividual(owlModel.getNextAnonymousResourceName());
        iA.addPropertyValue(propP, anonA);
        anonA.addPropertyValue(propP, anonB);
        anonB.addPropertyValue(propP, anonC);
        doCheck();
    }


    public void testDatatypeRelationship() {
        iA.addPropertyValue(propR, owlModel.createRDFSLiteral("33", owlModel.getXSDint()));
        doCheck();
    }
}

