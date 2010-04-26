package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLMaxCardinality;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeleteRowTestCase extends AbstractConditionsTableTestCase {

    /**
     * owl:Thing
     * Person
     * Animal
     * Person
     * ---------------------------------------
     * <sufficient>
     * <necessary>
     * Animal            -> delete
     * owl:Thing
     */
    public void testRemoveSuperclass() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        personCls.addSuperclass(animalCls);
        ConditionsTableModel tableModel = getTableModel(personCls);
        assertTrue(tableModel.isRemoveEnabledFor(2));
        assertTrue(tableModel.isRemoveEnabledFor(3));
        tableModel.deleteRow(tableModel.getClassRow(animalCls));
        assertEquals(1, personCls.getSuperclassCount());
        assertEquals(owlThing, personCls.getSuperclasses(false).iterator().next());
    }


    public void testDeleteOWLThingWithNamedDefinition() {
        OWLNamedClass c = owlModel.createOWLNamedClass("C");
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        c.setDefinition(a);

        ConditionsTableModel tableModel = getTableModel(c);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                a,
                NECESSARY,
                owlThing
        });

        assertTrue(tableModel.isDeleteEnabledFor(owlThing));
        tableModel.deleteRow(3);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                a,
                NECESSARY
        });
    }


    public void testDeleteOWLThingWithoutNamedDefinition() {
        OWLNamedClass c = owlModel.createOWLNamedClass("C");
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        c.setDefinition(owlModel.createOWLComplementClass(a));

        ConditionsTableModel tableModel = getTableModel(c);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                OWLComplementClass.class,
                NECESSARY,
                owlThing
        });

        assertFalse(tableModel.isDeleteEnabledFor(owlThing));
    }


    /**
     * <sufficient>
     * children >= 1     -> delete
     * <necessary>
     * owl:Thing
     * ------------------------------------------------------
     * <sufficient>
     * <necessary>
     * owl:Thing
     */
    public void testDeleteSingleAnonEquivalentClass() {
        OWLNamedClass parentCls = owlModel.createOWLNamedClass("Parent");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLRestriction restriction = owlModel.createOWLMinCardinality(property, 1);
        parentCls.setDefinition(restriction);
        assertEquals(1, parentCls.getEquivalentClasses().size());

        ConditionsTableModel tableModel = getTableModel(parentCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                restriction,
                NECESSARY,
                owlThing
        });
        assertFalse(tableModel.isRemoveEnabledFor(0));
        assertFalse(tableModel.isRemoveEnabledFor(1));
        assertTrue(tableModel.isDeleteEnabledFor(restriction));
        assertFalse(tableModel.isRemoveEnabledFor(2));
        assertFalse(tableModel.isRemoveEnabledFor(3));
        tableModel.deleteRow(tableModel.getClassRow(restriction));
        assertEquals(0, parentCls.getEquivalentClasses().size());
        assertEquals(1, parentCls.getSuperclassCount());
    }


    /**
     * <sufficient>
     * Male           -> delete
     * <necessary>
     * owl:Thing
     * ------------------------------------------------------
     * <sufficient>
     * <necessary>
     * owl:Thing
     */
    public void testDeleteSingleNamedEquivalentClass() {
        OWLNamedClass maleCls = owlModel.createOWLNamedClass("Male");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        cls.setDefinition(maleCls);
        assertEquals(1, maleCls.getEquivalentClasses().size());
        assertEquals(1, cls.getEquivalentClasses().size());
        assertEquals(2, cls.getSuperclassCount());
        ConditionsTableModel tableModel = getTableModel(cls);
        assertTableModelStructure(tableModel, new Object[]{
                null,
                maleCls,
                null,
                owlThing
        });
        assertFalse(tableModel.isRemoveEnabledFor(0));
        assertTrue(tableModel.isRemoveEnabledFor(1));
        assertFalse(tableModel.isRemoveEnabledFor(2));
        assertTrue(tableModel.isRemoveEnabledFor(3));
        tableModel.deleteRow(1);
        assertEquals(0, cls.getEquivalentClasses().size());
        assertEquals(1, cls.getSuperclassCount());
        assertEquals(owlThing, cls.getSuperclasses(false).iterator().next());
    }


    /**
     * owl:Thing
     * Animal
     * Parent   = Animal & children >= 1
     * ---------------------------------------
     * <sufficient>
     * Animal
     * children >= 1     -> delete
     * <necessary>
     */
    public void testDeleteAnonOperandFromTupleDefinition() {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass parentCls = owlModel.createOWLNamedClass("Parent");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLRestriction restriction = owlModel.createOWLMinCardinality(property, 1);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(animalCls);
        intersectionCls.addOperand(restriction);
        parentCls.addSuperclass(animalCls);
        parentCls.removeSuperclass(owlThing);
        parentCls.setDefinition(intersectionCls);
        assertEquals(1, parentCls.getEquivalentClasses().size());

        ConditionsTableModel tableModel = getTableModel(parentCls);
        assertEquals(4, tableModel.getRowCount());
        int i = 0;
        assertFalse(tableModel.isRemoveEnabledFor(i++));
        assertTrue(tableModel.isRemoveEnabledFor(i++));
        assertFalse(tableModel.isRemoveEnabledFor(i++));
        assertTrue(tableModel.isDeleteEnabledFor(restriction));
        assertFalse(tableModel.isRemoveEnabledFor(i++));
        int oldClsCount = owlModel.getClsCount();
        tableModel.deleteRow(tableModel.getClassRow(restriction));
        assertEquals(oldClsCount - 2, owlModel.getClsCount());
        assertEquals(1, parentCls.getSuperclassCount());
        assertEquals(1, parentCls.getEquivalentClasses().size());
        assertEquals(animalCls, parentCls.getDefinition());
    }


    /**
     * owl:Thing
     * Animal
     * Parent   = Animal & children >= 1
     * ---------------------------------------
     * <sufficient>
     * Animal            -> delete
     * children >= 1
     * <necessary>
     */
    public void testDeleteNamedOperandFromTupleDefinition() {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass parentCls = owlModel.createOWLNamedClass("Parent");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLRestriction restriction = owlModel.createOWLMinCardinality(property, 1);
        String restrictionText = restriction.getBrowserText();
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(animalCls);
        intersectionCls.addOperand(restriction);
        parentCls.addSuperclass(animalCls);
        parentCls.removeSuperclass(owlThing);
        parentCls.setDefinition(intersectionCls);
        assertEquals(1, parentCls.getEquivalentClasses().size());

        ConditionsTableModel tableModel = getTableModel(parentCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                animalCls,
                OWLMinCardinality.class,
                NECESSARY
        });
        int i = 0;
        assertFalse(tableModel.isRemoveEnabledFor(i++));
        assertTrue(tableModel.isRemoveEnabledFor(i++));
        assertFalse(tableModel.isRemoveEnabledFor(i++));
        assertFalse(tableModel.isRemoveEnabledFor(i++));
        int oldClsCount = owlModel.getClsCount();

        tableModel.deleteRow(tableModel.getClassRow(animalCls));
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                OWLMinCardinality.class,
                NECESSARY,
                owlThing
        });

        assertEquals(oldClsCount - 1, owlModel.getClsCount());
        assertEquals(2, parentCls.getSuperclassCount());
        assertEquals(owlThing, parentCls.getPureSuperclasses().iterator().next());
        assertEquals(1, parentCls.getEquivalentClasses().size());
        assertEquals(restrictionText, parentCls.getDefinition().getBrowserText());
    }


    /**
     * owl:Thing
     * Animal
     * Parent   = children >= 1  &  children <= 4  &  Animal
     * ---------------------------------------
     * <sufficient>
     * Animal            -> delete
     * children >= 1
     * children <= 4
     * <necessary>
     */
    public void testDeleteNamedOperandFromTripleDefinition() {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass parentCls = owlModel.createOWLNamedClass("Parent");
        parentCls.addSuperclass(animalCls);
        parentCls.removeSuperclass(owlThing);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLRestriction minRestriction = owlModel.createOWLMinCardinality(property, 1);
        OWLRestriction maxRestriction = owlModel.createOWLMaxCardinality(property, 4);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(minRestriction);
        intersectionCls.addOperand(maxRestriction);
        String expectedText = intersectionCls.getBrowserText();
        intersectionCls.addOperand(animalCls);
        parentCls.setDefinition(intersectionCls);
        assertEquals(1, parentCls.getEquivalentClasses().size());

        ConditionsTableModel tableModel = getTableModel(parentCls);
        assertEquals(5, tableModel.getRowCount());
        int i = 0;
        assertFalse(tableModel.isRemoveEnabledFor(i++));
        assertTrue(tableModel.isRemoveEnabledFor(i++));
        assertFalse(tableModel.isRemoveEnabledFor(i++));
        assertFalse(tableModel.isRemoveEnabledFor(i++));
        assertFalse(tableModel.isRemoveEnabledFor(i++));
        assertTrue(tableModel.isDeleteEnabledFor(minRestriction));
        assertTrue(tableModel.isDeleteEnabledFor(maxRestriction));
        int oldClsCount = owlModel.getClsCount();
        tableModel.deleteRow(tableModel.getClassRow(animalCls));
        assertEquals(oldClsCount, owlModel.getClsCount());
        assertEquals(2, parentCls.getSuperclassCount());
        assertEquals(owlThing, parentCls.getPureSuperclasses().iterator().next());
        assertEquals(1, parentCls.getEquivalentClasses().size());
        assertEquals(expectedText, parentCls.getDefinition().getBrowserText());
    }


    /**
     * owl:Thing
     * Animal
     * Parent   = Animal  &  children >= 1  &  children <= 4
     * ---------------------------------------
     * <sufficient>
     * Animal
     * children >= 1
     * children <= 4     -> delete
     * <necessary>
     */
    public void testDeleteAnonOperandFromTripleDefinition() {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass parentCls = owlModel.createOWLNamedClass("Parent");
        parentCls.addSuperclass(animalCls);
        parentCls.removeSuperclass(owlThing);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLRestriction minRestriction = owlModel.createOWLMinCardinality(property, 1);
        OWLRestriction maxRestriction = owlModel.createOWLMaxCardinality(property, 4);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(animalCls);
        intersectionCls.addOperand(minRestriction);
        String expectedText = intersectionCls.getBrowserText();
        intersectionCls.addOperand(maxRestriction);
        parentCls.setDefinition(intersectionCls);
        assertEquals(1, parentCls.getEquivalentClasses().size());

        ConditionsTableModel tableModel = getTableModel(parentCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                animalCls,
                OWLMinCardinality.class,
                OWLMaxCardinality.class,
                NECESSARY
        });
        int oldClsCount = owlModel.getClsCount();
        tableModel.deleteRow(3);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                animalCls,
                OWLMinCardinality.class,
                NECESSARY
        });

        assertEquals(expectedText, parentCls.getDefinition().getBrowserText());
    }


    /**
     * <sufficient>
     * Male             -> delete
     * children >= 1
     * <sufficient>
     * Male
     * <necessary>
     */
    public void testAldi() {
        System.setProperty(ConditionsTableModel.SHOW_INHERITED_RESTRICTIONS, ConditionsTableModel.USE_INFERENCE);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLNamedClass maleCls = owlModel.createOWLNamedClass("Male");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        cls.addEquivalentClass(maleCls);
        cls.removeSuperclass(owlThing);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(maleCls);
        intersectionCls.addOperand(owlModel.createOWLMinCardinality(property, 1));
        cls.addEquivalentClass(intersectionCls);
        ConditionsTableModel tableModel = getTableModel(cls);
        assertTableModelStructure(tableModel, new Object[]{
                null,
                maleCls,
                OWLMinCardinality.class,
                null,
                maleCls,
                null
        });
        tableModel.deleteRow(1);
        assertTableModelStructure(tableModel, new Object[]{
                null,
                maleCls,
                null,
                OWLMinCardinality.class,
                null
        });
        System.getProperties().remove(ConditionsTableModel.SHOW_INHERITED_RESTRICTIONS);
    }


    /**
     * owl:Thing
     * Person    (= Mensch)  -> delete row Mensch from Person
     * Mensch
     * Person
     * ------------------------------------------------------
     * Must make sure that Mensch has at least one superclass
     */
    public void testDeleteOnlySuperclass() {
        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
        OWLNamedClass menschClass = owlModel.createOWLNamedClass("Mensch");
        personClass.addEquivalentClass(menschClass);
        menschClass.removeSuperclass(owlThing);
        ConditionsTableModel tableModel = getTableModel(personClass);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                menschClass,
                NECESSARY,
                owlThing
        });
        tableModel.deleteRow(1);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing
        });
        assertSize(1, menschClass.getSuperclasses(false));
        assertEquals(personClass, menschClass.getSuperclasses(false).iterator().next());
    }


    public void testIsRemoveEnabledInMixedClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLNamedClass dc = owlModel.createOWLNamedClass("Other");
        OWLIntersectionClass in = owlModel.createOWLIntersectionClass();
        in.addOperand(dc);
        in.addOperand(owlModel.createOWLComplementClass(dc));
        cls.setDefinition(in);
        ConditionsTableModel tableModel = getTableModel(cls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                dc,
                OWLComplementClass.class,
                NECESSARY,
                owlThing
        });
        assertTrue(tableModel.isRemoveEnabledFor(4));
    }


    public void testDeleteLastNamedSuperclass() {
        OWLNamedClass superclass = owlModel.createOWLNamedClass("Superclass");
        OWLNamedClass c = owlModel.createOWLNamedSubclass("Class", superclass);
        c.addSuperclass(owlModel.createOWLComplementClass(superclass));
        ConditionsTableModel tableModel = getTableModel(c);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                superclass,
                OWLComplementClass.class
        });
        assertTrue(tableModel.isRemoveEnabledFor(2));
        tableModel.deleteRow(2);
        assertSize(2, c.getSuperclasses(false));
        assertContains(owlThing, c.getSuperclasses(false));
    }


    public void testDeleteLastNamedSuperclassOWLThing() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        c.addSuperclass(owlModel.createOWLComplementClass(c));
        ConditionsTableModel tableModel = getTableModel(c);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing,
                OWLComplementClass.class
        });
        assertFalse(tableModel.isRemoveEnabledFor(2));
    }
}
