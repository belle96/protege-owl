package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SetConditionFailedTestCase extends AbstractConditionsTableTestCase {

    /**
     * <sufficient>
     * <necessary>
     * Animal       -> set to "!Animal"
     * --------------------------------------------
     * owl:Thing
     * Person   (!Animal)
     * Animal
     */
    public void testReplaceLastNamedSuperclassWithAnonCls() throws Exception {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        cls.addSuperclass(animalCls);
        cls.removeSuperclass(owlThing);
        ConditionsTableModel tableModel = getTableModel(cls);
        assertTrue(tableModel.isSeparator(0));
        assertTrue(tableModel.isSeparator(1));
        assertEquals(animalCls, tableModel.getClass(2));
        tableModel.setValueAt(2, owlModel, "not Animal");
        assertEquals(2, cls.getSuperclassCount());
        assertTrue(cls.isSubclassOf(owlThing));
        Collection ss = new ArrayList(cls.getSuperclasses(false));
        ss.remove(owlThing);
        assertTrue(ss.iterator().next() instanceof OWLComplementClass);
        assertEquals(0, cls.getEquivalentClasses().size());
    }


    /**
     * <sufficient>
     * <necessary>
     * Animal
     * !Animal     -> set to "children >= 1"
     * --------------------------------------------
     * owl:Thing
     * Animal
     * Person   (children >= 1)
     */
    public void testReplaceAnonSuperclassWithRestriction() throws Exception {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        cls.addSuperclass(animalCls);
        cls.addSuperclass(owlModel.createOWLComplementClass(animalCls));
        cls.removeSuperclass(owlThing);
        ConditionsTableModel tableModel = getTableModel(cls);
        assertEquals(4, tableModel.getRowCount());
        assertTrue(tableModel.isSeparator(0));
        assertTrue(tableModel.isSeparator(1));
        assertEquals(animalCls, tableModel.getClass(2));
        assertTrue(tableModel.getClass(3) instanceof OWLComplementClass);
        tableModel.setValueAt(3, owlModel, "children min 1");
        assertEquals(2, cls.getSuperclassCount());
        assertTrue(cls.isSubclassOf(animalCls));
        Collection ss = new ArrayList(cls.getSuperclasses(false));
        ss.remove(animalCls);
        assertTrue(ss.iterator().next() instanceof OWLMinCardinality);
        assertEquals(0, cls.getEquivalentClasses().size());
    }


    /**
     * <sufficient>
     * <necessary>
     * Animal
     * owl:Thing  -> set to "children >= 1"
     * !Animal
     * --------------------------------------------
     * owl:Thing
     * Animal
     * Person   (children >= 1  ,  !Animal)
     */
    public void testReplaceNamedSuperclassWithRestriction() throws Exception {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        cls.addSuperclass(animalCls);
        cls.addSuperclass(owlModel.createOWLComplementClass(animalCls));
        ConditionsTableModel tableModel = getTableModel(cls);
        assertEquals(5, tableModel.getRowCount());
        assertTrue(tableModel.isSeparator(0));
        assertTrue(tableModel.isSeparator(1));
        assertEquals(animalCls, tableModel.getClass(2));
        assertEquals(owlThing, tableModel.getClass(3));
        assertTrue(tableModel.getClass(4) instanceof OWLComplementClass);
        tableModel.setValueAt(3, owlModel, "children min 1");
        assertEquals(3, cls.getSuperclassCount());
        assertTrue(cls.isSubclassOf(animalCls));
        assertEquals(0, cls.getEquivalentClasses().size());
    }


    /**
     * <sufficient>
     * Mensch       -> set to "!Mensch"
     * <necessary>
     * ------------------------------------------------------
     * owl:Thing
     * Person    = !Mensch
     * Mensch
     */
    public void testSetNamedEquivalentClsToAnon() throws Exception {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass menschCls = owlModel.createOWLNamedClass("Mensch");
        personCls.addEquivalentClass(menschCls);
        personCls.removeSuperclass(owlThing);
        menschCls.removeSuperclass(owlThing);
        assertFalse(menschCls.getSuperclasses(false).contains(owlThing));
        ConditionsTableModel tableModel = getTableModel(personCls);
        assertEquals(3, tableModel.getRowCount());
        assertTrue(tableModel.isSeparator(0));
        assertEquals(menschCls, tableModel.getClass(1));
        assertTrue(tableModel.isSeparator(2));
        tableModel.setValueAt(1, owlModel, "not Mensch");
        assertEquals(2, personCls.getSuperclassCount());
        assertEquals(1, personCls.getEquivalentClasses().size());
        assertTrue(personCls.getDefinition() instanceof OWLComplementClass);
        assertTrue(personCls.isSubclassOf(owlThing));
        assertEquals(0, menschCls.getEquivalentClasses().size());
        assertEquals(1, menschCls.getSuperclassCount());
        assertTrue(menschCls.isSubclassOf(owlThing));
    }


    /**
     * <sufficient>
     * !Mensch       -> set to "Mensch"
     * <necessary>
     * owl:Thing
     * ------------------------------------------------------
     * owl:Thing
     * Person    = Mensch
     * Mensch    = Person
     */
    public void testSetAnonEquivalentClsToNamed() throws Exception {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass menschCls = owlModel.createOWLNamedClass("Mensch");
        cls.addEquivalentClass(owlModel.createOWLComplementClass(menschCls));
        ConditionsTableModel tableModel = getTableModel(cls);
        assertEquals(4, tableModel.getRowCount());
        assertTrue(tableModel.isSeparator(0));
        assertTrue(tableModel.getClass(1) instanceof OWLComplementClass);
        assertTrue(tableModel.isSeparator(2));
        assertEquals(owlThing, tableModel.getClass(3));
        int oldClsCount = owlModel.getClsCount();
        tableModel.setValueAt(1, owlModel, "Mensch");
        assertEquals(oldClsCount - 1, owlModel.getClsCount());
        assertEquals(2, cls.getSuperclassCount());
        assertEquals(1, cls.getEquivalentClasses().size());
        assertEquals(menschCls, cls.getDefinition());
        assertTrue(cls.isSubclassOf(owlThing));
    }


    /**
     * <sufficient>
     * !Mensch       -> set to "!Animal"
     * <necessary>
     * Animal
     * ------------------------------------------------------
     * owl:Thing
     * Animal
     * Person    = !Animal
     */
    public void testSetAnonEquivalentClsToOtherAnon() throws Exception {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass menschCls = owlModel.createOWLNamedClass("Mensch");
        cls.addSuperclass(animalCls);
        cls.removeSuperclass(owlThing);
        cls.addEquivalentClass(owlModel.createOWLComplementClass(menschCls));
        ConditionsTableModel tableModel = getTableModel(cls);
        assertEquals(4, tableModel.getRowCount());
        assertTrue(tableModel.isSeparator(0));
        assertTrue(tableModel.getClass(1) instanceof OWLComplementClass);
        assertTrue(tableModel.isSeparator(2));
        assertEquals(animalCls, tableModel.getClass(3));
        int oldClsCount = owlModel.getClsCount();
        tableModel.setValueAt(1, owlModel, "not Animal");
        assertEquals(oldClsCount, owlModel.getClsCount());
        assertEquals(2, cls.getSuperclassCount());
        assertEquals(1, cls.getEquivalentClasses().size());
        assertTrue(cls.getDefinition() instanceof OWLComplementClass);
        assertEquals(animalCls, ((OWLComplementClass) cls.getDefinition()).getComplement());
        assertTrue(cls.isSubclassOf(animalCls));
    }


    /**
     * <sufficient>
     * Animal
     * !Tier       -> set to "!Animal"
     * <necessary>
     * ------------------------------------------------------
     * owl:Thing
     * Animal
     * Person    = Animal & !Animal
     */
    public void testSetAnonEquivalentClsOperandToOtherAnon() throws Exception {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass tierCls = owlModel.createOWLNamedClass("Tier");
        cls.addSuperclass(animalCls);
        cls.removeSuperclass(owlThing);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(animalCls);
        intersectionCls.addOperand(owlModel.createOWLComplementClass(tierCls));
        cls.addEquivalentClass(intersectionCls);
        ConditionsTableModel tableModel = getTableModel(cls);
        assertEquals(4, tableModel.getRowCount());
        assertTrue(tableModel.isSeparator(0));
        assertEquals(animalCls, tableModel.getClass(1));
        assertTrue(tableModel.getClass(2) instanceof OWLComplementClass);
        assertTrue(tableModel.isSeparator(3));
        int oldClsCount = owlModel.getClsCount();
        tableModel.setValueAt(2, owlModel, "not Animal");
        assertEquals(oldClsCount, owlModel.getClsCount());
        assertEquals(2, cls.getSuperclassCount());
        assertEquals(1, cls.getEquivalentClasses().size());
        assertTrue(cls.getDefinition() instanceof OWLIntersectionClass);
        OWLIntersectionClass newDefinition = (OWLIntersectionClass) cls.getDefinition();
        final Collection newOperands = new ArrayList(newDefinition.getOperands());
        assertTrue(newOperands.contains(animalCls));
        newOperands.remove(animalCls);
        assertTrue(newOperands.iterator().next() instanceof OWLComplementClass);
        assertEquals(animalCls, ((OWLComplementClass) newOperands.iterator().next()).getComplement());
        assertTrue(cls.isSubclassOf(animalCls));
    }


    /**
     * <sufficient>
     * Animal      -> set to "!Animal"
     * !Tier
     * <necessary>
     * ------------------------------------------------------
     * owl:Thing
     * Person    = !Animal & !Tier
     */
    public void testSetNamedEquivalentClsOperandToAnon() throws Exception {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass tierCls = owlModel.createOWLNamedClass("Tier");
        personCls.addSuperclass(animalCls);
        personCls.removeSuperclass(owlThing);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(animalCls);
        intersectionCls.addOperand(owlModel.createOWLComplementClass(tierCls));
        personCls.addEquivalentClass(intersectionCls);
        ConditionsTableModel tableModel = getTableModel(personCls);
        assertEquals(4, tableModel.getRowCount());
        assertTrue(tableModel.isSeparator(0));
        assertEquals(animalCls, tableModel.getClass(1));
        assertTrue(tableModel.getClass(2) instanceof OWLComplementClass);
        assertTrue(tableModel.isSeparator(3));
        int oldClsCount = owlModel.getClsCount();
        tableModel.setValueAt(1, owlModel, "not Animal");
        assertEquals(oldClsCount + 1, owlModel.getClsCount());
        assertEquals(2, personCls.getSuperclassCount());
        assertEquals(1, personCls.getEquivalentClasses().size());
        assertTrue(personCls.getDefinition() instanceof OWLIntersectionClass);
        OWLIntersectionClass newDefinition = (OWLIntersectionClass) personCls.getDefinition();
        final List newOperands = new ArrayList(newDefinition.getOperands());
        assertTrue(newOperands.get(0) instanceof OWLComplementClass);
        assertTrue(newOperands.get(1) instanceof OWLComplementClass);
        assertTrue(personCls.isSubclassOf(owlThing));
    }


    /**
     * <sufficient>
     * Animal      -> set to "Tier"
     * !Tier
     * <necessary>
     * ------------------------------------------------------
     * owl:Thing
     * Tier
     * Person    = Tier & !Tier
     */
    public void testSetNamedEquivalentClsOperandToOtherNamed() throws Exception {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass tierCls = owlModel.createOWLNamedClass("Tier");
        cls.addSuperclass(animalCls);
        cls.removeSuperclass(owlThing);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(animalCls);
        intersectionCls.addOperand(owlModel.createOWLComplementClass(tierCls));
        cls.addEquivalentClass(intersectionCls);
        ConditionsTableModel tableModel = getTableModel(cls);
        assertEquals(4, tableModel.getRowCount());
        assertTrue(tableModel.isSeparator(0));
        assertEquals(animalCls, tableModel.getClass(1));
        assertTrue(tableModel.getClass(2) instanceof OWLComplementClass);
        assertTrue(tableModel.isSeparator(3));
        int oldClsCount = owlModel.getClsCount();
        tableModel.setValueAt(1, owlModel, "Tier");
        assertEquals(oldClsCount, owlModel.getClsCount());
        assertEquals(2, cls.getSuperclassCount());
        assertEquals(1, cls.getEquivalentClasses().size());
        assertTrue(cls.getDefinition() instanceof OWLIntersectionClass);
        OWLIntersectionClass newDefinition = (OWLIntersectionClass) cls.getDefinition();
        Collection newOperands = new ArrayList(newDefinition.getOperands());
        assertTrue(newOperands.contains(tierCls));
        newOperands.remove(tierCls);
        assertTrue(newOperands.iterator().next() instanceof OWLComplementClass);
        assertTrue(cls.isSubclassOf(tierCls));
    }


    /**
     * <sufficient>
     * children >= 1   -> reassign unchanged
     * <necessary>
     * owl:Thing
     * --------------------------------------
     * Nothing should happen
     */
    public void testSetUnchanged() throws Exception {
        RDFProperty property = owlModel.createOWLObjectProperty("children");
        OWLRestriction restriction = owlModel.createOWLMinCardinality(property, 1);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Parent");
        cls.setDefinition(restriction);
        ConditionsTableModel tableModel = getTableModel(cls);
        tableModel.setValueAt(1, owlModel, "children min 1");
        assertEquals(1, cls.getEquivalentClasses().size());
        assertEquals(restriction, cls.getDefinition());
    }


    public void testSetToItself() throws Exception {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        ConditionsTableModel tableModel = getTableModel(cls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing
        });
        tableModel.setValueAt(2, owlModel, cls.getName());
        assertSize(1, cls.getSuperclasses(false));
        assertEquals(owlThing, cls.getFirstSuperclass());
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing
        });
    }
}
