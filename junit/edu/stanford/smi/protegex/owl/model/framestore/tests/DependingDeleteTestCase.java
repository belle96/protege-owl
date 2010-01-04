package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collections;

/**
 * A TestCase to test the OWLFrameStore' recursive delete of depending classes.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DependingDeleteTestCase extends AbstractJenaTestCase {

    public void testDeleteHasRestrictionOnInstanceDelete() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLProperty genderProperty = owlModel.createOWLObjectProperty("gender");
        OWLNamedClass genderCls = owlModel.createOWLNamedClass("Gender");
        Instance male = genderCls.createInstance("male");
        genderProperty.setUnionRangeClasses(Collections.singleton(genderCls));
        OWLNamedClass malePerson = owlModel.createOWLNamedClass("MalePerson");
        malePerson.addSuperclass(personCls);
        int oldCount = owlModel.getClsCount();
        OWLHasValue hasRestriction = owlModel.createOWLHasValue(genderProperty, male);
        malePerson.addSuperclass(hasRestriction);
        male.delete();
        assertEquals(oldCount, owlModel.getClsCount());
    }


    public void testDeleteAllAndSomeRestrictionOnClsDelete() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass malePersonCls = owlModel.createOWLNamedClass("MalePerson");
        malePersonCls.addSuperclass(personCls);
        OWLProperty property = owlModel.createOWLObjectProperty("children");
        property.setUnionRangeClasses(Collections.singleton(personCls));
        property.addUnionDomainClass(personCls);
        OWLAllValuesFrom allRestriction = owlModel.createOWLAllValuesFrom(property, malePersonCls);
        personCls.addSuperclass(allRestriction);
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(malePersonCls);
        OWLSomeValuesFrom someRestriction = owlModel.createOWLSomeValuesFrom(property, complementCls);
        personCls.addSuperclass(someRestriction);
        int oldCount = owlModel.getClsCount();
        malePersonCls.delete();
        assertEquals(oldCount - 4, owlModel.getClsCount());
    }


    public void testDeleteRestrictionsOnSlotDelete() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass malePersonCls = owlModel.createOWLNamedClass("MalePerson");
        malePersonCls.addSuperclass(personCls);
        OWLProperty property = owlModel.createOWLObjectProperty("children");
        property.setUnionRangeClasses(Collections.singleton(personCls));
        property.addUnionDomainClass(personCls);
        int oldCount = owlModel.getClsCount();
        OWLAllValuesFrom allRestriction = owlModel.createOWLAllValuesFrom(property, malePersonCls);    // 1
        personCls.addSuperclass(allRestriction);
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(malePersonCls);             // 2
        OWLSomeValuesFrom someRestriction = owlModel.createOWLSomeValuesFrom(property, complementCls); // 3
        personCls.addEquivalentClass(someRestriction);
        OWLMaxCardinality maxCardiRestriction = owlModel.createOWLMaxCardinality(property, 4); // 4
        personCls.addDisjointClass(maxCardiRestriction);
        OWLHasValue hasRestriction = owlModel.createOWLHasValue(property, "Tetzel");         // 5
        personCls.addDisjointClass(hasRestriction);
        property.delete();
        assertEquals(oldCount, owlModel.getClsCount());
    }


    public void testDeleteRestrictionOnSuperSlotDelete() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLProperty superProperty = owlModel.createOWLObjectProperty("superProperty");
        OWLProperty subProperty = owlModel.createOWLObjectProperty("subProperty");
        subProperty.addSuperproperty(superProperty);
        int clsCount = owlModel.getClsCount();
        cls.addSuperclass(owlModel.createOWLMinCardinality(subProperty, 1));
        superProperty.delete();
        assertEquals(clsCount, owlModel.getClsCount());
    }


    public void testDeleteSomeRestrictionOfInheritedCls() {
        OWLObjectProperty habitatSlot = owlModel.createOWLObjectProperty("hasHabitat");
        OWLNamedClass koalaCls = owlModel.createOWLNamedClass("Koala");
        int oldCount = owlModel.getClsCount();
        OWLNamedClass forestCls = owlModel.createOWLNamedClass("Forest");
        OWLNamedClass dryForestCls = owlModel.createOWLNamedSubclass("DryEucalyptForest", forestCls);
        koalaCls.addSuperclass(owlModel.createOWLSomeValuesFrom(habitatSlot, dryForestCls));
        forestCls.delete();
        assertEquals(oldCount, owlModel.getClsCount());
    }


    public void testDeleteAnonymousDatatypeInRestriction() {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        RDFSDatatype datatype = owlModel.createRDFSDatatype(owlModel.getNextAnonymousResourceName());
        OWLAllValuesFrom allValuesFrom = owlModel.createOWLAllValuesFrom(property, datatype);
        int oldCount = owlModel.getRDFSDatatypeClass().getInstances(true).size();
        allValuesFrom.delete();
        int newCount = owlModel.getRDFSDatatypeClass().getInstances(true).size();
        assertEquals(oldCount - 1, newCount);
    }


    public void testDeleteAnonymousDatatypeInRange() {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        RDFSDatatype datatype = owlModel.createRDFSDatatype(owlModel.getNextAnonymousResourceName());
        property.setRange(datatype);
        int oldCount = owlModel.getRDFSDatatypeClass().getInstances(true).size();
        property.delete();
        int newCount = owlModel.getRDFSDatatypeClass().getInstances(true).size();
        assertEquals(oldCount - 1, newCount);
    }
}
