package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLMaxCardinality;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ChangeConditionsTableModelTestCase extends AbstractConditionsTableTestCase {

    /**
     * owl:Thing
     * Person
     * ---------------------------------------
     * Add Animal as superclass of Person
     * ---------------------------------------
     * <sufficient>
     * <necessary>
     * Animal
     * owl:Thing
     */
    public void testAddDirectSuperclass() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        ConditionsTableModel tableModel = getTableModel(personCls);
        assertFalse(tableModel.isRemoveEnabledFor(0));
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        personCls.addSuperclass(animalCls);
        assertEquals(4, tableModel.getRowCount());
        int i = 2;
        assertEquals(animalCls, tableModel.getClass(i));
        assertTrue(tableModel.isRemoveEnabledFor(i));
        i++;
        assertEquals(owlModel.getRootCls(), tableModel.getClass(i));
        assertTrue(tableModel.isRemoveEnabledFor(i));
        assertTrue(events.size() > 0);
    }


    public void testRemoveDirectSuperclass() {
        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
        OWLNamedClass animalClass = owlModel.createOWLNamedClass("Animal");
        personClass.addSuperclass(animalClass);
        ConditionsTableModel tableModel = getTableModel(personClass);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                animalClass,
                owlThing
        });
        assertTrue(tableModel.isRemoveEnabledFor(2));
        assertTrue(tableModel.isRemoveEnabledFor(3));
        personClass.removeSuperclass(animalClass);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing
        });
        assertEquals(owlThing, tableModel.getClass(2));
        assertFalse(tableModel.isRemoveEnabledFor(2));
        assertTrue(events.size() > 0);
    }


    /**
     * owl:Thing
     * Person
     * Mensch
     * ------------------------------------------------------
     * Add Mensch to equivalent classes of Person and remove owl:Thing from superclasses
     * ------------------------------------------------------
     * <sufficient>
     * Mensch
     * <necessary>
     */
    public void testAddNamedEquivalentClass() {
        OWLNamedClass menschCls = owlModel.createOWLNamedClass("Mensch");
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        ConditionsTableModel tableModel = getTableModel(personCls);
        personCls.addEquivalentClass(menschCls);
        personCls.removeSuperclass(owlThing);
        assertEquals(3, tableModel.getRowCount());
        assertEquals(menschCls, tableModel.getClass(1));
        assertTrue(tableModel.isDefinition(1));
        assertTrue(events.size() > 0);
    }


    /**
     * <sufficient>
     * Male            -> Move to other definition block
     * children >= 1
     * <sufficient>
     * children <= 4
     * <necessary>
     * ------------------------------------------------------
     * Male must remain direct superclass of the class
     */
    public void testMoveNamedEquivalentClass() {
        OWLNamedClass maleCls = owlModel.createOWLNamedClass("Male");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(maleCls);
        intersectionCls.addOperand(owlModel.createOWLMinCardinality(property, 1));
        cls.addEquivalentClass(intersectionCls);
        cls.addEquivalentClass(owlModel.createOWLMaxCardinality(property, 4));
        cls.addSuperclass(maleCls);
        cls.removeSuperclass(owlThing);
        ConditionsTableModel tableModel = getTableModel(cls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                maleCls,
                OWLMinCardinality.class,
                SUFFICIENT,
                OWLMaxCardinality.class,
                NECESSARY
        });
        tableModel.addRow(maleCls, 4);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                maleCls,
                OWLMaxCardinality.class,
                SUFFICIENT,
                maleCls,
                OWLMinCardinality.class,
                NECESSARY
        });
        tableModel.deleteRow(1);
        assertTrue(cls.isSubclassOf(maleCls));
    }


    public void testDeleteRestrictionViaPropertyDelete() {
        OWLNamedClass namedClass = owlModel.createOWLNamedClass("Class");
        RDFProperty property = owlModel.createOWLDatatypeProperty("property");
        namedClass.addSuperclass(owlModel.createOWLMinCardinality(property, 1));
        ConditionsTableModel tableModel = getTableModel(namedClass);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing,
                OWLMinCardinality.class
        });
        property.delete();
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing
        });
    }
}
