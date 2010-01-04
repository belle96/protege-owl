package edu.stanford.smi.protegex.owl.model.util.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.util.ClosureAxiomFactory;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClosureAxiomFactoryTestCase extends AbstractJenaTestCase {


    public void testAddClosureAxiomToPrimitiveClass() {
        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");

        OWLNamedClass fatherClass = owlModel.createOWLNamedClass("Father");
        OWLNamedClass motherClass = owlModel.createOWLNamedClass("Mother");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("hasParent");
        OWLSomeValuesFrom fatherRestriction = owlModel.createOWLSomeValuesFrom(property, fatherClass);
        OWLSomeValuesFrom motherRestriction = owlModel.createOWLSomeValuesFrom(property, motherClass);
        personClass.addSuperclass(fatherRestriction);
        personClass.addSuperclass(motherRestriction);
        assertNull(ClosureAxiomFactory.getClosureAxiom(personClass, fatherRestriction));
        assertNull(ClosureAxiomFactory.getClosureAxiom(personClass, motherRestriction));

        OWLAllValuesFrom closure = ClosureAxiomFactory.addClosureAxiom(personClass, fatherRestriction);

        assertEquals(4, personClass.getSuperclassCount());
        assertContains(closure, personClass.getPureSuperclasses());
        assertEquals(property, closure.getOnProperty());
        OWLUnionClass filler = (OWLUnionClass) closure.getFiller();
        assertContains(fatherClass, filler.getOperands());
        assertContains(motherClass, filler.getOperands());
        assertSize(2, filler.getOperands());

        assertEquals(closure, ClosureAxiomFactory.getClosureAxiom(personClass, fatherRestriction));
        assertEquals(closure, ClosureAxiomFactory.getClosureAxiom(personClass, motherRestriction));
    }


    public void testAddClosureAxiomToSimpleDefinedClass() {
        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");

        OWLNamedClass fatherClass = owlModel.createOWLNamedClass("Father");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("hasParent");
        OWLSomeValuesFrom restriction = owlModel.createOWLSomeValuesFrom(property, fatherClass);
        personClass.setDefinition(restriction);
        assertNull(ClosureAxiomFactory.getClosureAxiom(personClass, restriction));
            

        OWLAllValuesFrom closure = ClosureAxiomFactory.addClosureAxiom(personClass, restriction);

        assertEquals(2, personClass.getSuperclassCount());
        OWLIntersectionClass newIntersectionClass = (OWLIntersectionClass) personClass.getDefinition();
        assertEquals("(hasParent some Father) and (hasParent only Father)",
                newIntersectionClass.getBrowserText());

        //TT - invalid test. restriction is deleted in the call ClosureAxiomFactory.addClosureAxiom
        //assertEquals(closure, ClosureAxiomFactory.getClosureAxiom(personClass, restriction)); 
    }


    public void testAddClosureAxiomToComplexDefinedClass() {
        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
        OWLNamedClass fatherClass = owlModel.createOWLNamedClass("Father");
        OWLNamedClass motherClass = owlModel.createOWLNamedClass("Mother");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("hasParent");
        OWLSomeValuesFrom fatherRestriction = owlModel.createOWLSomeValuesFrom(property, fatherClass);
        OWLSomeValuesFrom motherRestriction = owlModel.createOWLSomeValuesFrom(property, motherClass);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(fatherRestriction);
        intersectionCls.addOperand(motherRestriction);
        personClass.setDefinition(intersectionCls);
        assertNull(ClosureAxiomFactory.getClosureAxiom(personClass, fatherRestriction));
        assertNull(ClosureAxiomFactory.getClosureAxiom(personClass, motherRestriction));

        OWLAllValuesFrom closure = ClosureAxiomFactory.addClosureAxiom(personClass, fatherRestriction);

        assertEquals(2, personClass.getSuperclassCount());
        OWLIntersectionClass newIntersectionClass = (OWLIntersectionClass) personClass.getDefinition();
        assertEquals("(hasParent some Father) and (hasParent some Mother) and (hasParent only (Father or Mother))",
                newIntersectionClass.getBrowserText());
        
        //TT - invalid tests. restriction is deleted in the call ClosureAxiomFactory.addClosureAxiom
        //assertEquals(closure, ClosureAxiomFactory.getClosureAxiom(personClass, fatherRestriction));
        //assertEquals(closure, ClosureAxiomFactory.getClosureAxiom(personClass, motherRestriction));
    }


    public void testAddClosureAxiomForOWLHasValue() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        RDFResource filler = owlThing.createOWLIndividual("Individual");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLHasValue restriction = owlModel.createOWLHasValue(property, filler);
        namedClass.addSuperclass(restriction);
        assertNull(ClosureAxiomFactory.getClosureAxiom(namedClass, restriction));

        OWLAllValuesFrom closure = ClosureAxiomFactory.addClosureAxiom(namedClass, restriction);

        assertTrue(closure.getAllValuesFrom() instanceof OWLEnumeratedClass);
        OWLEnumeratedClass enumClass = (OWLEnumeratedClass) closure.getAllValuesFrom();
        assertSize(1, enumClass.getOneOf());
        assertContains(filler, enumClass.getOneOf());

        assertEquals(closure, ClosureAxiomFactory.getClosureAxiom(namedClass, restriction));
    }
}
