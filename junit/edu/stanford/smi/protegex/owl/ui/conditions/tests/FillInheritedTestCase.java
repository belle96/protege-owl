package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.OWLAllValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLHasValue;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FillInheritedTestCase extends AbstractConditionsTableTestCase {

    /**
     * owl:Thing
     * Animal      children <= 100
     * Person    children <= 12
     * Parent  children >= 1
     * ---------------------------------------
     * <sufficient>
     * <necessary>
     * Person
     * children >= 1
     * <inherited>
     * children <= 12
     */
    public void testInheritedRestrictions() {

        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");

        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLRestriction animalRestriction = owlModel.createOWLMaxCardinality(property, 100);
        animalCls.addSuperclass(animalRestriction);

        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        personCls.addSuperclass(animalCls);
        personCls.removeSuperclass(owlModel.getOWLThingClass());
        OWLRestriction personRestriction = owlModel.createOWLMaxCardinality(property, 12);
        personCls.addSuperclass(personRestriction);

        OWLNamedClass parentCls = owlModel.createOWLNamedClass("Parent");
        parentCls.addSuperclass(personCls);
        parentCls.removeSuperclass(owlModel.getOWLThingClass());
        OWLRestriction parentRestriction = owlModel.createOWLMinCardinality(property, 1);
        parentCls.addSuperclass(parentRestriction);

        ConditionsTableModel tableModel = getTableModel(parentCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                personCls,
                parentRestriction,
                INHERITED,
                personRestriction
        });
        assertEquals(TYPE_SUPERCLASS, tableModel.getType(2));
        assertEquals(TYPE_SUPERCLASS, tableModel.getType(3));
        assertEquals(personCls, tableModel.getOriginClass(5));
        assertEquals(TYPE_INHERITED, tableModel.getType(5));
    }


    /**
     * owl:Thing
     * Animal
     * Person  =  Animal & (gender >= 1)
     * MalePerson                       *
     * --------------------------------------------
     * <sufficient>
     * <necessary>
     * Person
     * <inherited>
     * gender >= 1
     */
    public void testInheritedFromDefinition() {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass maleCls = owlModel.createOWLNamedClass("MalePerson");
        maleCls.addSuperclass(personCls);
        maleCls.removeSuperclass(owlModel.getOWLThingClass());
        OWLObjectProperty genderProperty = owlModel.createOWLObjectProperty("gender");
        OWLRestriction restriction = owlModel.createOWLMinCardinality(genderProperty, 1);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(animalCls);
        intersectionCls.addOperand(restriction);
        personCls.setDefinition(intersectionCls);

        ConditionsTableModel tableModel = getTableModel(maleCls);
        assertEquals(5, tableModel.getRowCount());
        int i = 2;
        assertEquals(personCls, tableModel.getClass(i));
        assertEquals(TYPE_SUPERCLASS, tableModel.getType(i));
        i++;
        assertTrue(tableModel.isSeparator(i));
        i++;
        assertEquals(restriction, tableModel.getClass(i));
        assertEquals(TYPE_INHERITED, tableModel.getType(i));
    }


    /**
     * owl:Thing
     * Animal
     * Marsupial  =  Koala
     * Koala (!Person)
     * ------------------------------------------------------
     * <sufficient>
     * Marsupial
     * <necessary>
     * !Person
     */
    public void testDontInheritDuplicatesFromEquivalentCls() {
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLNamedClass marsupialCls = owlModel.createOWLNamedClass("Marsuipial");
        OWLNamedClass koalaCls = owlModel.createOWLNamedClass("Koala");
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        marsupialCls.addSuperclass(animalCls);
        marsupialCls.removeSuperclass(owlThing);
        marsupialCls.addEquivalentClass(koalaCls);
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(personCls);
        koalaCls.addSuperclass(complementCls);
        koalaCls.removeSuperclass(owlThing);
        ConditionsTableModel tableModel = getTableModel(koalaCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                marsupialCls,
                NECESSARY,
                complementCls
        });
    }


    /**
     * owl:Thing
     * Super       (* slot Super)
     * Sub       (* slot Sub)
     * ------------------------------------------------------
     * <sufficient>
     * <necessary>
     * Super
     * * slot Sub
     */
    public void testDontInheritOverloadedAllRestrictions() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass superCls = (OWLNamedClass) owlModel.createOWLNamedSubclass("Super", owlThing);
        superCls.addSuperclass(owlModel.createOWLAllValuesFrom(property, superCls));
        OWLNamedClass subCls = owlModel.createOWLNamedSubclass("Sub", superCls);
        subCls.addSuperclass(owlModel.createOWLAllValuesFrom(property, subCls));
        ConditionsTableModel tableModel = getTableModel(subCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                superCls,
                OWLAllValuesFrom.class
        });
    }


    public void testInheritUnrelatedAllRestrictions() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass clsA = (OWLNamedClass) owlModel.createOWLNamedSubclass("A", owlThing);
        clsA.addSuperclass(owlModel.createOWLAllValuesFrom(property, clsA));
        OWLNamedClass clsB = (OWLNamedClass) owlModel.createOWLNamedSubclass("B", owlThing);
        clsB.addSuperclass(owlModel.createOWLAllValuesFrom(property, clsB));
        OWLNamedClass cls = (OWLNamedClass) owlModel.createOWLNamedSubclass("C", clsA);
        cls.addSuperclass(clsB);
        ConditionsTableModel tableModel = getTableModel(cls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                clsA,
                clsB,
                INHERITED,
                OWLAllValuesFrom.class,
                OWLAllValuesFrom.class
        });
    }


    /**
     * owl:Thing
     * Super       (slot $ mySuper)
     * Sub       (slot $ mySub)
     */
    public void testInheritHasRestrictions() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");

        OWLNamedClass superCls = (OWLNamedClass) owlModel.createOWLNamedSubclass("Super", owlThing);
        Instance superInstance = superCls.createInstance("mySuper");
        superCls.addSuperclass(owlModel.createOWLHasValue(property, superInstance));

        OWLNamedClass subCls = owlModel.createOWLNamedSubclass("Sub", superCls);
        Instance subInstance = superCls.createInstance("mySub");
        subCls.addSuperclass(owlModel.createOWLHasValue(property, subInstance));

        ConditionsTableModel tableModel = getTableModel(subCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                superCls,
                OWLHasValue.class,
                INHERITED,
                OWLHasValue.class
        });
    }


    /**
     * owl:Thing
     * Other
     * Super       (? slot Other)
     * Sub       (? slot Sub)
     */
    public void testInheritParallelSomeRestrictions() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");

        OWLNamedClass superCls = (OWLNamedClass) owlModel.createOWLNamedSubclass("Super", owlThing);
        superCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, otherCls));

        OWLNamedClass subCls = owlModel.createOWLNamedSubclass("Sub", superCls);
        subCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, subCls));

        ConditionsTableModel tableModel = getTableModel(subCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                superCls,
                OWLSomeValuesFrom.class,
                INHERITED,
                OWLSomeValuesFrom.class
        });
    }


    /**
     * owl:Thing
     * Super       (? slot Super)
     * Sub       (? slot Sub)
     */
    public void testInheritSpecializingSomeRestrictions() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");

        OWLNamedClass superCls = (OWLNamedClass) owlModel.createOWLNamedSubclass("Super", owlThing);
        superCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, superCls));

        OWLNamedClass subCls = owlModel.createOWLNamedSubclass("Sub", superCls);
        subCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, subCls));

        ConditionsTableModel tableModel = getTableModel(subCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                superCls,
                OWLSomeValuesFrom.class
        });
    }


    /**
     * owl:Thing
     * Super       (? slot Super)
     * Sub       (? slot Super)
     */
    public void testInheritEqualSomeRestriction() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass superCls = (OWLNamedClass) owlModel.createOWLNamedSubclass("Super", owlThing);
        superCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, superCls));
        OWLNamedClass subCls = owlModel.createOWLNamedSubclass("Sub", superCls);
        subCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, superCls));
        ConditionsTableModel tableModel = getTableModel(subCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                superCls,
                OWLSomeValuesFrom.class
        });
    }
}
