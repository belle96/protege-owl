package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SetPropertyTypeTestCase extends AbstractJenaTestCase {

    public void testDeleteRestrictionsOnD2O() {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Cls");
        hostCls.addSuperclass(owlModel.createOWLMinCardinality(property, 1));
        int oldCount = owlModel.getClsCount();
        hostCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, owlModel.getXSDstring()));
        property.setProtegeType(owlModel.getOWLObjectPropertyClass());
        assertEquals(oldCount, owlModel.getClsCount());
        assertEquals(owlModel.getOWLObjectPropertyClass(), property.getProtegeType());
        assertNull(property.getRange());
    }


    public void testDeleteRestrictionsOnO2D() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Cls");
        hostCls.addSuperclass(owlModel.createOWLMinCardinality(property, 1));
        int oldCount = owlModel.getClsCount();
        hostCls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, hostCls));
        property.setProtegeType(owlModel.getOWLDatatypePropertyClass());
        assertEquals(oldCount, owlModel.getClsCount());
        assertEquals(owlModel.getOWLDatatypePropertyClass(), property.getProtegeType());
        assertNull(property.getRange());
    }


    public void testDeleteNestedRestrictionsOnD2O() {
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDstring());
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Cls");
        hostCls.addSuperclass(owlModel.createOWLMinCardinality(slot, 1));
        int oldCount = owlModel.getClsCount();
        OWLUnionClass unionCls = owlModel.createOWLUnionClass();
        unionCls.addOperand(owlModel.createOWLSomeValuesFrom(slot, owlModel.getXSDstring()));
        unionCls.addOperand(owlModel.createOWLSomeValuesFrom(slot, owlModel.getXSDint()));
        hostCls.addSuperclass(unionCls);
        slot.setProtegeType(owlModel.getOWLObjectPropertyClass());
        assertEquals(oldCount, owlModel.getClsCount());
        assertEquals(owlModel.getOWLObjectPropertyClass(), slot.getProtegeType());
    }
}
