package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLFrameStoreTestCase extends AbstractJenaTestCase {

    /**
     * owl:Thing
     * Test     -> Make equivalent with intersection, remove owl:Thing
     * (Operand & !Test)
     * Operand
     * -----------------------------------------------------------------
     * owl:Thing
     * Operand
     * Test      (= Operand & !Test)
     */
    public void testAddIntersectionClass() {
        OWLNamedClass testCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass operandCls = owlModel.createOWLNamedClass("Person");
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(operandCls);
        intersectionCls.addOperand(owlModel.createOWLComplementClass(testCls));
        intersectionCls.addSuperclass(testCls);
        assertEquals(1, testCls.getSuperclassCount());
        assertEquals(0, testCls.getEquivalentClasses().size());
        assertEquals(1, intersectionCls.getSuperclassCount());
        assertEquals(0, intersectionCls.getEquivalentClasses().size());

        testCls.addSuperclass(intersectionCls);
        testCls.removeSuperclass(owlThing);
        assertEquals(2, testCls.getSuperclassCount());
        assertEquals(0, testCls.getPureSuperclasses().size());
        assertTrue(testCls.isSubclassOf(operandCls));
        assertEquals(1, testCls.getEquivalentClasses().size());
        assertEquals(1, intersectionCls.getSuperclassCount());
        assertEquals(1, intersectionCls.getEquivalentClasses().size());
    }


    /**
     * owl:Thing
     * Test
     * Operand
     * (Operand & !Test)    -> Make equivalent with Test
     * Test
     * -----------------------------------------------------------------
     * owl:Thing
     * Test      (= Operand & !Test)
     * Operand
     * Test
     */
    public void testAddNamedClsToIntersectionClass() {
        OWLNamedClass testCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass operandCls = owlModel.createOWLNamedClass("Person");
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(operandCls);
        intersectionCls.addOperand(owlModel.createOWLComplementClass(testCls));
        testCls.addSuperclass(intersectionCls);
        assertEquals(2, testCls.getSuperclassCount());
        assertEquals(0, testCls.getEquivalentClasses().size());
        assertEquals(0, intersectionCls.getSuperclassCount());
        assertEquals(0, intersectionCls.getEquivalentClasses().size());

        intersectionCls.addSuperclass(testCls);
        testCls.removeSuperclass(owlThing);
        assertEquals(2, testCls.getSuperclassCount());
        assertEquals(0, testCls.getPureSuperclasses().size());
        assertTrue(testCls.isSubclassOf(operandCls));
        assertEquals(1, testCls.getEquivalentClasses().size());
        assertEquals(1, intersectionCls.getSuperclassCount());
        assertEquals(1, intersectionCls.getEquivalentClasses().size());
    }


    /**
     * owl:Thing
     * Operand
     * Test      (= Operand & !Test)   -> Remove intersection
     * ----------------------------------------------------------
     * owl:Thing
     * Test
     */
    public void testRemoveIntersectionCls() {
        OWLNamedClass testCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass operandCls = owlModel.createOWLNamedClass("Person");
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(operandCls);
        intersectionCls.addOperand(owlModel.createOWLComplementClass(testCls));
        testCls.addEquivalentClass(intersectionCls);
        testCls.removeSuperclass(owlThing);
        assertSize(2, testCls.getSuperclasses(false));
        assertSize(0, testCls.getPureSuperclasses());
        assertSize(1, testCls.getEquivalentClasses());
        assertSize(1, intersectionCls.getEquivalentClasses());

        testCls.removeSuperclass(intersectionCls);
        assertSize(0, testCls.getSuperclasses(false));
    }


    /**
     * owl:Thing
     * Operand
     * Test      (= Operand & !Test)   -> Remove Test
     * ----------------------------------------------------------
     * owl:Thing
     * Test
     */
    public void testRemoveNamedClsFromIntersectionCls() {
        OWLNamedClass testCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass operandCls = owlModel.createOWLNamedClass("Person");
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(operandCls);
        intersectionCls.addOperand(owlModel.createOWLComplementClass(testCls));
        testCls.addEquivalentClass(intersectionCls);
        testCls.removeSuperclass(owlThing);
        assertSize(2, testCls.getSuperclasses(false));
        assertSize(0, testCls.getPureSuperclasses());
        assertSize(1, testCls.getEquivalentClasses());
        assertSize(1, intersectionCls.getEquivalentClasses());

        intersectionCls.removeSuperclass(testCls);
        assertSize(1, testCls.getSuperclasses(false));
        assertTrue(testCls.getSuperclasses(false).iterator().next() instanceof OWLIntersectionClass);
    }


    /**
     * owl:Thing
     * Animal
     * Person   -> Add superclass Animal
     * ------------------------------------------------------
     * No duplicates can be added as superclasses
     */
    public void testDuplicateSuperclass() {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass personCls = owlModel.createOWLNamedSubclass("Person", animalCls);
        Collection oldSuperclasses = personCls.getSuperclasses(false);
        personCls.addSuperclass(animalCls);
        assertEquals(oldSuperclasses, personCls.getSuperclasses(false));
    }


    /**
     * owl:Thing
     * Test  = Operand
     * = Operand & !Operand   -> delete
     * ------------------------------------------------------
     * owl:Thing
     * Test  = Operand
     */
    public void testDeleteEquivalentClsWithEquivalentNamedCls() {
        OWLNamedClass testCls = owlModel.createOWLNamedClass("Test");
        OWLNamedClass operandCls = owlModel.createOWLNamedClass("Operand");
        testCls.addEquivalentClass(operandCls);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(operandCls);
        intersectionCls.addOperand(owlModel.createOWLComplementClass(operandCls));
        testCls.addEquivalentClass(intersectionCls);
        assertSize(2, testCls.getEquivalentClasses());
        assertSize(1, testCls.getPureSuperclasses());
        assertEquals(owlThing, testCls.getPureSuperclasses().iterator().next());
        assertSize(3, testCls.getSuperclasses(false));

        intersectionCls.removeSuperclass(testCls);
        assertSize(1, testCls.getEquivalentClasses());
        assertSize(2, testCls.getPureSuperclasses());
        testCls.removeSuperclass(intersectionCls);
        assertSize(1, testCls.getPureSuperclasses());
        assertEquals(owlThing, testCls.getPureSuperclasses().iterator().next());
        assertSize(2, testCls.getSuperclasses(false));
    }


    /**
     * owl:Thing
     * R
     * A
     * B   -> Add Root as direct superclass
     * ------------------------------------------------------
     * owl:Thing
     * R
     * A
     * B
     * B
     */
    public void testAddSuperclass() {
        OWLNamedClass r = owlModel.createOWLNamedClass("R");
        OWLNamedClass a = owlModel.createOWLNamedSubclass("A", r);
        OWLNamedClass b = owlModel.createOWLNamedSubclass("B", a);
        assertEquals(1, b.getSuperclassCount());
        b.addSuperclass(r);
        assertEquals(2, b.getSuperclassCount());
        assertTrue(b.getSuperclasses(false).contains(r));
        assertTrue(b.getSuperclasses(false).contains(a));
    }
}
