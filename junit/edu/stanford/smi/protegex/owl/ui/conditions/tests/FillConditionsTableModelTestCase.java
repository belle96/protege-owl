package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import java.util.Arrays;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.OWLHasValue;
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
public class FillConditionsTableModelTestCase extends AbstractConditionsTableTestCase {

    public void testEmptyClass() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        ConditionsTableModel tableModel = getTableModel(personCls);
        assertEquals(3, tableModel.getRowCount());
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing
        });
        assertTrue(tableModel.isCellEditable(2, COL_EXPRESSION));
        assertFalse(tableModel.isRemoveEnabledFor(2));
    }


    public void testSingleRestrictionClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLRestriction restriction = owlModel.createOWLMinCardinality(property, 1);
        cls.addSuperclass(restriction);
        ConditionsTableModel tableModel = getTableModel(cls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing,
                restriction
        });
        assertTrue(tableModel.isCellEditable(2, COL_EXPRESSION));
        assertTrue(tableModel.isCellEditable(3, COL_EXPRESSION));
        assertTrue(tableModel.isDeleteEnabledFor(restriction));
        assertFalse(tableModel.isRemoveEnabledFor(3));
    }


    /**
     * owl:Thing
     * Person  children >= 1
     * children <= 3
     * ? children Person
     * gender >= 1
     * * children Person
     * children $ hans
     * ------------------------------------------------------
     * <sufficient>
     * <necessary>
     * owl:Thing
     * * children Person
     * ? children Person
     * children $ hans
     * children >= 1
     * children <= 3
     * gender >= 1
     */
    public void testSortRestrictions() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty childrenProperty = owlModel.createOWLObjectProperty("children");
        OWLObjectProperty genderProperty = owlModel.createOWLObjectProperty("gender");
        OWLRestriction minCardiRestriction = owlModel.createOWLMinCardinality(childrenProperty, 1);
        personCls.addSuperclass(minCardiRestriction);
        OWLRestriction maxCardiRestriction = owlModel.createOWLMaxCardinality(childrenProperty, 3);
        personCls.addSuperclass(maxCardiRestriction);
        OWLRestriction someRestriction = owlModel.createOWLSomeValuesFrom(childrenProperty, personCls);
        personCls.addSuperclass(someRestriction);
        OWLRestriction genderRestriction = owlModel.createOWLMinCardinality(genderProperty, 1);
        personCls.addSuperclass(genderRestriction);
        OWLRestriction allRestriction = owlModel.createOWLAllValuesFrom(childrenProperty, personCls);
        personCls.addSuperclass(allRestriction);
        OWLRestriction hasRestriction = owlModel.createOWLHasValue(childrenProperty, personCls.createInstance("hans"));
        personCls.addSuperclass(hasRestriction);
        ConditionsTableModel tableModel = getTableModel(personCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing,
                allRestriction,
                someRestriction,
                hasRestriction,
                minCardiRestriction,
                maxCardiRestriction,
                genderRestriction
        });
    }


    /**
     * owl:Thing
     * Person    children >= 1     * children Person
     * ------------------------------------------------------
     * <sufficient>
     * <necessary>
     * owl:Thing
     * * children Person
     * children >= 1
     */
    public void testTwoRestrictionsClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLRestriction minRestriction = owlModel.createOWLMinCardinality(property, 1);
        cls.addSuperclass(minRestriction);
        OWLRestriction allRestriction = owlModel.createOWLAllValuesFrom(property, cls);
        cls.addSuperclass(allRestriction);
        ConditionsTableModel tableModel = getTableModel(cls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing,
                allRestriction,
                minRestriction
        });
    }


    /**
     * owl:Thing
     * Person
     * MalePerson   = Person  &  gender $ male
     * ------------------------------------------------------
     * <sufficient>
     * Person  (=)
     * gender $ male (=)
     * <necessary>
     */
    public void testBothSuperclassAndEquivalentClass() {
        OWLObjectProperty genderProperty = owlModel.createOWLObjectProperty("gender");
        OWLNamedClass genderCls = owlModel.createOWLNamedClass("Gender");
        Instance male = genderCls.createInstance("male");
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass malePersonCls = owlModel.createOWLNamedClass("MalePerson");
        malePersonCls.addSuperclass(personCls);
        malePersonCls.removeSuperclass(owlModel.getOWLThingClass());
        OWLHasValue hasRestriction = owlModel.createOWLHasValue(genderProperty, male);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass(Arrays.asList(new Cls[]{
                personCls,
                hasRestriction
        }));
        malePersonCls.setDefinition(intersectionCls);
        ConditionsTableModel tableModel = getTableModel(malePersonCls);
        assertEquals(4, tableModel.getRowCount());
        int i = 0;
        assertTrue(tableModel.isSeparator(i));
        i++;
        assertEquals(personCls, tableModel.getClass(i));
        assertEquals(TYPE_DEFINITION_BASE, tableModel.getType(i));
        assertEquals(i, tableModel.getClassRow(personCls));
        assertEquals(intersectionCls, tableModel.getDefinition(i));
        i++;
        assertEquals(hasRestriction, tableModel.getClass(i));
        assertEquals(TYPE_DEFINITION_BASE, tableModel.getType(i));
        assertEquals(intersectionCls, tableModel.getDefinition(i));
        i++;
        assertTrue(tableModel.isSeparator(i));
    }


    /**
     * owl:Thing
     * Mensch
     * Person
     * Aldi
     * Person
     * Person    = Mensch  =  children >= 1  =  (children <= 4 & Aldi)
     * ------------------------------------------------------
     * <sufficient>
     * Aldi
     * children <= 4
     * <sufficient>
     * Mensch
     * <sufficient>
     * children >= 1
     * <necessary>
     * owl:Thing
     */
    public void testMultipleEquivalentClasses() {
        OWLNamedClass aldiCls = owlModel.createOWLNamedClass("Aldi");
        OWLNamedClass menschCls = owlModel.createOWLNamedClass("Mensch");
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLRestriction minRestriction = owlModel.createOWLMinCardinality(property, 1);
        OWLRestriction maxRestriction = owlModel.createOWLMaxCardinality(property, 4);
        ConditionsTableModel tableModel = getTableModel(personCls);
        tableModel.addRow(menschCls, 0);
        personCls.addEquivalentClass(minRestriction);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(maxRestriction);
        intersectionCls.addOperand(aldiCls);
        personCls.addEquivalentClass(intersectionCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                aldiCls,
                OWLMaxCardinality.class,
                SUFFICIENT,
                menschCls,
                SUFFICIENT,
                OWLMinCardinality.class,
                NECESSARY,
                owlThing
        });
    }


    /**
     * owl:Thing
     * Mensch
     * Person
     * Aldi
     * Person
     * Person    = (Aldi & children <= 4)  =  children >= 1  =  Mensch
     * -----------------------------------------------------------------
     * <sufficient>
     * Aldi
     * children <= 4
     * <sufficient>
     * Mensch
     * <sufficient>
     * children >= 1
     * <necessary>
     * owl:Thing
     */
    public void testMultipleEquivalentClassesPreordered() {
        OWLNamedClass aldiCls = owlModel.createOWLNamedClass("Aldi");
        OWLNamedClass menschCls = owlModel.createOWLNamedClass("Mensch");
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        OWLRestriction minRestriction = owlModel.createOWLMinCardinality(property, 1);
        OWLRestriction maxRestriction = owlModel.createOWLMaxCardinality(property, 4);
        ConditionsTableModel tableModel = getTableModel(personCls);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(aldiCls);
        intersectionCls.addOperand(maxRestriction);
        tableModel.addRow(intersectionCls, 0);
        tableModel.addEmptyDefinitionBlock();
        tableModel.addRow(menschCls, 0);
        tableModel.addEmptyDefinitionBlock();
        tableModel.addRow(minRestriction, 0);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                aldiCls,
                OWLMaxCardinality.class,
                SUFFICIENT,
                menschCls,
                SUFFICIENT,
                OWLMinCardinality.class,
                NECESSARY,
                owlThing
        });
    }
}
