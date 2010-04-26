package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import java.util.Collection;
import java.util.Iterator;

import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddRowTestCase extends AbstractConditionsTableTestCase {

    /**
     * <sufficient>
     * <necessary>   -> Add Animal
     * owl:Thing
     * ------------------------------------------------------
     * owl:Thing
     * Person
     * Animal
     * Person
     */
    public void testAddAtSuperclassSeparator() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        ConditionsTableModel tableModel = getTableModel(personCls);
        assertTrue(tableModel.isSeparator(1));
        tableModel.addRow(animalCls, 1);
        assertEquals(2, personCls.getSuperclassCount());
        assertEquals(0, personCls.getEquivalentClasses().size());
        assertTrue(personCls.isSubclassOf(animalCls));
        assertTrue(personCls.isSubclassOf(owlThing));
        assertTrue(events.size() > 0);
    }


    /**
     * <sufficient>
     * <necessary>
     * owl:Thing     -> Add Animal
     * ------------------------------------------------------
     * owl:Thing
     * Person
     * Animal
     * Person
     */
    public void testAddAtSuperclass() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        ConditionsTableModel tableModel = getTableModel(personCls);
        assertEquals(owlThing, tableModel.getClass(2));
        tableModel.addRow(animalCls, 2);
        assertEquals(2, personCls.getSuperclassCount());
        assertEquals(0, personCls.getEquivalentClasses().size());
        assertTrue(personCls.isSubclassOf(animalCls));
        assertTrue(personCls.isSubclassOf(owlThing));
        assertTrue(events.size() > 0);
    }


    /**
     * <sufficient>  -> Add Mensch
     * <necessary>
     * owl:Thing
     * ------------------------------------------------------
     * owl:Thing
     * Person   = Mensch
     */
    public void testAddSingleEquivalentClassAtSeparator() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass menschCls = owlModel.createOWLNamedClass("Mensch");
        ConditionsTableModel tableModel = getTableModel(personCls);
        assertTrue(tableModel.isSeparator(0));
        tableModel.addRow(menschCls, 0);
        assertEquals(2, personCls.getSuperclassCount());
        assertEquals(1, personCls.getPureSuperclasses().size());
        assertEquals(1, personCls.getEquivalentClasses().size());
        assertTrue(personCls.hasEquivalentClass(menschCls));
        assertTrue(personCls.isSubclassOf(owlThing));
        assertTrue(events.size() > 0);
    }


    /**
     * <sufficient>   -> Add Other
     * Mensch
     * <necessary>
     * owl:Thing
     * ------------------------------------------------------
     * owl:Thing
     * Person   = Mensch = Other
     * Mensch
     * Person
     * Other
     * Person
     */
    public void testAddNamedEquivalentClassAtSeparator() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass menschCls = owlModel.createOWLNamedClass("Mensch");
        personCls.addEquivalentClass(menschCls);
        ConditionsTableModel tableModel = getTableModel(personCls);
        assertTrue(tableModel.isSeparator(0));
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        int oldClsCount = owlModel.getClsCount();
        personCls.addEquivalentClass(otherCls);
        assertEquals(oldClsCount, owlModel.getClsCount());
        assertEquals(3, personCls.getSuperclassCount());
        assertEquals(1, personCls.getPureSuperclasses().size());
        assertEquals(2, personCls.getEquivalentClasses().size());
        assertTrue(personCls.hasEquivalentClass(menschCls));
        assertTrue(personCls.hasEquivalentClass(otherCls));
        assertTrue(personCls.isSubclassOf(owlThing));
        assertTrue(events.size() > 0);
    }


    /**
     * <sufficient>   -> Add Other
     * Mensch
     * <necessary>
     * owl:Thing
     * ------------------------------------------------------
     * owl:Thing
     * Person   = Mensch & Other
     * Mensch
     * Person
     * Other
     * Person
     */
    public void testAddNamedEquivalentClassAtDefinition() {
        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
        OWLNamedClass menschClass = owlModel.createOWLNamedClass("Mensch");
        personClass.addEquivalentClass(menschClass);
        ConditionsTableModel tableModel = getTableModel(personClass);
        tableModel.dumpItems();
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                menschClass,
                NECESSARY,
                owlThing
        });
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        assertEquals(menschClass, tableModel.getClass(1));
        int oldClsCount = owlModel.getClsCount();
        tableModel.addRow(otherCls, 1);
        assertEquals(oldClsCount + 1, owlModel.getClsCount());
        assertSize(4, personClass.getSuperclasses(false));
        assertSize(1, personClass.getPureSuperclasses());
        assertTrue(personClass.getPureSuperclasses().contains(owlThing));
        assertSize(1, personClass.getEquivalentClasses());
        assertFalse(personClass.hasEquivalentClass(menschClass));
        OWLIntersectionClass definition = (OWLIntersectionClass) personClass.getEquivalentClasses().iterator().next();
        assertTrue(definition.getOperands().contains(menschClass));
        assertTrue(definition.getOperands().contains(otherCls));
        assertTrue(events.size() > 0);
    }


    /**
     * <sufficient>
     * Mensch         -> Add children >= 1
     * Other
     * <necessary>
     * owl:Thing
     * ------------------------------------------------------
     * owl:Thing
     * Person   = Mensch & Other & children >= 1
     * Mensch
     * Person
     * Other
     * Person
     */
    public void testAddAnonEquivalentClassAtDefinition() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass menschCls = owlModel.createOWLNamedClass("Mensch");
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        ConditionsTableModel tableModel = getTableModel(personCls);
        tableModel.addRow(menschCls, 0);
        tableModel.addRow(otherCls, 1);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                menschCls,
                otherCls,
                NECESSARY,
                owlThing
        });
        OWLObjectProperty childrenProperty = owlModel.createOWLObjectProperty("children");
        OWLRestriction restriction = owlModel.createOWLMinCardinality(childrenProperty, 1);
        int oldClsCount = owlModel.getClsCount();

        tableModel.addRow(restriction, 1);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                menschCls,
                otherCls,
                OWLMinCardinality.class,
                NECESSARY,
                owlThing
        });

        assertEquals(oldClsCount, owlModel.getClsCount());
        assertSize(4, personCls.getSuperclasses(false));
        assertSize(1, personCls.getPureSuperclasses());
        assertTrue(personCls.isSubclassOf(owlThing));
        assertSize(1, personCls.getEquivalentClasses());
        OWLIntersectionClass definition = (OWLIntersectionClass) personCls.getEquivalentClasses().iterator().next();
        final Collection operands = definition.getOperands();
        assertEquals(3, operands.size());
        assertTrue(operands.contains(menschCls));
        assertTrue(operands.contains(otherCls));
        assertTrue(events.size() > 0);
    }


    /**
     * <sufficient>
     * Mensch
     * <sufficient>
     * Other        -> Add children >= 1
     * <necessary>
     * owl:Thing
     * ------------------------------------------------------
     * owl:Thing
     * Person   = Mensch & Other & children >= 1
     * Mensch
     * Person
     * Other
     * Person
     */
    public void testAddAnonEquivalentClassAtSecondDefinition() {
        System.setProperty(ConditionsTableModel.SHOW_INHERITED_RESTRICTIONS, ConditionsTableModel.USE_INFERENCE);
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass menschCls = owlModel.createOWLNamedClass("Mensch");
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        ConditionsTableModel tableModel = getTableModel(personCls);
        tableModel.addRow(menschCls, 0);
        tableModel.addEmptyDefinitionBlock();
        tableModel.addRow(otherCls, 0);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                menschCls,
                SUFFICIENT,
                otherCls,
                NECESSARY,
                owlThing
        });
        OWLObjectProperty childrenProperty = owlModel.createOWLObjectProperty("children");
        OWLRestriction restriction = owlModel.createOWLMinCardinality(childrenProperty, 1);
        int oldClsCount = owlModel.getClsCount();

        tableModel.addRow(restriction, 3);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                menschCls,
                SUFFICIENT,
                otherCls,
                OWLMinCardinality.class,
                NECESSARY,
                owlThing
        });
        assertEquals(oldClsCount + 1, owlModel.getClsCount());
        assertEquals(4, personCls.getSuperclassCount());
        assertEquals(1, personCls.getPureSuperclasses().size());
        assertTrue(personCls.isSubclassOf(owlThing));
        assertEquals(2, personCls.getEquivalentClasses().size());
        assertTrue(personCls.hasEquivalentClass(menschCls));
        assertFalse(personCls.hasEquivalentClass(otherCls));
        OWLIntersectionClass definition = null;
        for (Iterator it = personCls.getEquivalentClasses().iterator(); it.hasNext();) {
            RDFSClass equivalentClass = (RDFSClass) it.next();
            if (equivalentClass instanceof OWLIntersectionClass) {
                definition = (OWLIntersectionClass) equivalentClass;
            }
        }
        assertEquals(2, definition.getOperands().size());
        assertTrue(definition.getOperands().contains(otherCls));
        assertTrue(events.size() > 0);
        System.getProperties().remove(ConditionsTableModel.SHOW_INHERITED_RESTRICTIONS);
    }


    /**
     * <sufficient>  -> Add Animal
     * <necessary>
     * Animal
     * ------------------------------------------
     * Add rejected because no duplicates allowed
     */
    public void testAddDuplicateNamedClass() {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        personCls.addSuperclass(animalCls);
        personCls.removeSuperclass(owlThing);
        ConditionsTableModel tableModel = getTableModel(personCls);
        tableModel.addRow(animalCls, 0);
        assertEquals(1, personCls.getSuperclassCount());
    }


    /**
     * <sufficient>  -> Add children >= 1
     * children >= 1
     * <necessary>
     * owl:Thing
     * ------------------------------------------
     * <sufficient>
     * children >= 1
     * <sufficient>
     * children >= 1
     * <necessary>
     * owl:Thing
     */
    public void testAddDuplicateAnonClass() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Parent");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        ConditionsTableModel tableModel = getTableModel(personCls);
        tableModel.addRow(owlModel.createOWLMinCardinality(property, 1), 0);
        tableModel.addEmptyDefinitionBlock();
        tableModel.addRow(owlModel.createOWLMinCardinality(property, 1), 0);  // allowed
        assertEquals(3, personCls.getSuperclassCount());
        assertEquals(6, tableModel.getRowCount());
    }


    /**
     * <sufficient>
     * Animal
     * children >= 1    -> Add children >= 1
     * <necessary>
     * owl:Thing
     * ------------------------------------------
     * Add rejected because no duplicates allowed
     */
    public void testAddDuplicateOperand() {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Parent");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        ConditionsTableModel tableModel = getTableModel(personCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing
        });
        personCls.addEquivalentClass(animalCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                animalCls,
                NECESSARY,
                owlThing
        });
        assertContains(animalCls, personCls.getPropertyValues(owlModel.getOWLEquivalentClassProperty()));
        tableModel.addRow(owlModel.createOWLMinCardinality(property, 1), 1);
        assertTrue(personCls.getDefinition() instanceof OWLIntersectionClass);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                animalCls,
                OWLMinCardinality.class,
                NECESSARY,
                owlThing
        });
        tableModel.addRow(owlModel.createOWLMinCardinality(property, 1), 1);  // fails
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                animalCls,
                OWLMinCardinality.class,
                NECESSARY,
                owlThing
        });
        assertSize(3, personCls.getSuperclasses(false));
        OWLIntersectionClass intersectionCls = (OWLIntersectionClass) personCls.getDefinition();
        assertSize(2, intersectionCls.getOperands());
    }


    /**
     * <sufficient>
     * <necessary>
     * Animal         -> Add children >= 1
     * <inherited>
     * children >= 1
     * ------------------------------------------------------
     * owl:Thing
     * Animal   (children >= 1)
     * Person (children >= 1)
     */
    public void testAddInherited() {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Person", animalCls);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        animalCls.addSuperclass(owlModel.createOWLMinCardinality(property, 1));
        ConditionsTableModel tableModel = getTableModel(cls);
        int i = 0;
        assertTrue(tableModel.isSeparator(i++));
        assertTrue(tableModel.isSeparator(i++));
        assertEquals(animalCls, tableModel.getClass(i++));
        assertTrue(tableModel.isSeparator(i++));
        assertTrue(tableModel.getClass(i++) instanceof OWLMinCardinality);
        assertEquals(i, tableModel.getRowCount());
        OWLRestriction newRestriction = owlModel.createOWLMinCardinality(property, 1);
        assertTrue(tableModel.addRow(newRestriction, 2));
        assertEquals(2, cls.getSuperclassCount());
    }


    /**
     * <sufficient>   -> Add owl:Thing
     * <necessary>
     * ------------------------------------------------------
     * No change should happen
     */
    public void testDisallowEquivalentToThing() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        ConditionsTableModel tableModel = getTableModel(cls);
        assertFalse(tableModel.addRowAllowMove((RDFSClass) owlThing, 0));
        //assertEquals(0, events.size()); //TT- test not valid anymore
    }


    public void testAddIntersectionCls() {
        OWLNamedClass testCls = owlModel.createOWLNamedClass("Parent");
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLRestriction restriction = owlModel.createOWLMinCardinality(owlModel.createOWLObjectProperty("children"), 1);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(personCls);
        intersectionCls.addOperand(restriction);
        ConditionsTableModel tableModel = getTableModel(testCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing
        });
        tableModel.addRow(intersectionCls, 1);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing,
                intersectionCls
        });
    }
}
