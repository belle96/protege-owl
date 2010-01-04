package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class AllValuesFromRestrictionUpdaterTestCase extends AbstractJenaTestCase {

    public void testChangeAllRestrictions() {

        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("HappyPerson");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("OtherPerson");
        aCls.addSuperclass(personCls);
        bCls.addSuperclass(personCls);
        OWLProperty property = owlModel.createOWLObjectProperty("children");
        property.setUnionRangeClasses(Collections.singleton(personCls));
        property.addUnionDomainClass(personCls);
        int oldClsCount = owlModel.getClsCount();
        Facet valueTypeFacet = owlModel.getFacet(Model.Facet.VALUE_TYPE);

        // Add single OWLAllValuesFrom to Person: * children HappyPerson
        OWLAllValuesFrom aRestriction = owlModel.createOWLAllValuesFrom(property, aCls);
        personCls.addSuperclass(aRestriction);
        assertTrue(((Cls) personCls).hasDirectlyOverriddenTemplateFacet(property, valueTypeFacet));
        assertEquals(1, ((Cls) personCls).getTemplateSlotAllowedClses(property).size());
        assertEquals(aCls, ((Cls) personCls).getTemplateSlotAllowedClses(property).iterator().next());

        // Add second OWLAllValuesFrom to Person: * children OtherPerson
        OWLAllValuesFrom bRestriction = owlModel.createOWLAllValuesFrom(property, bCls);
        personCls.addSuperclass(bRestriction);
        assertFalse(((Cls) personCls).hasDirectlyOverriddenTemplateFacet(property, valueTypeFacet));
        personCls.removeSuperclass(aRestriction);
        assertEquals(1, ((Cls) personCls).getTemplateSlotAllowedClses(property).size());
        assertEquals(bCls, ((Cls) personCls).getTemplateSlotAllowedClses(property).iterator().next());
        personCls.removeSuperclass(bRestriction);
        assertFalse(((Cls) personCls).hasDirectlyOverriddenTemplateFacet(property, valueTypeFacet));

        OWLComplementClass complementCls = owlModel.createOWLComplementClass(aCls);
        OWLUnionClass union = owlModel.createOWLUnionClass(Arrays.asList(new Cls[]{complementCls, bCls}));
        OWLAllValuesFrom bothRestriction = owlModel.createOWLAllValuesFrom(property, union);
        personCls.addSuperclass(bothRestriction);
        Collection templateSlotAllowedClses = ((Cls) personCls).getTemplateSlotAllowedClses(property);
        assertEquals(2, templateSlotAllowedClses.size());
        assertTrue(templateSlotAllowedClses.contains(complementCls));
        assertTrue(templateSlotAllowedClses.contains(bCls));
        personCls.removeSuperclass(bothRestriction);

        RDFSClass intersection = owlModel.createOWLIntersectionClass(Arrays.asList(new Cls[]{aCls, owlModel.createOWLComplementClass(bCls)}));
        OWLAllValuesFrom intersectionRestriction = owlModel.createOWLAllValuesFrom(property, intersection);
        personCls.addSuperclass(intersectionRestriction);
        templateSlotAllowedClses = ((Cls) personCls).getTemplateSlotAllowedClses(property);
        assertEquals(1, templateSlotAllowedClses.size());
        personCls.removeSuperclass(intersectionRestriction);
        assertFalse(((Cls) personCls).hasDirectlyOverriddenTemplateFacet(property, valueTypeFacet));

        assertEquals(oldClsCount, owlModel.getClsCount());
    }


    public void testComplexAllRestrictions() {

        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");
        OWLNamedClass cCls = owlModel.createOWLNamedClass("C");
        bCls.addSuperclass(aCls);
        cCls.addSuperclass(aCls);
        OWLProperty property = owlModel.createOWLObjectProperty("children");
        property.setUnionRangeClasses(Collections.singleton(aCls));
        property.addUnionDomainClass(aCls);
        int oldClsCount = owlModel.getClsCount();

        Facet valueTypeFacet = owlModel.getFacet(Model.Facet.VALUE_TYPE);
        OWLUnionClass unionCls = owlModel.createOWLUnionClass(Arrays.asList(new Cls[]{bCls, cCls}));
        RDFSClass complementClass = owlModel.createOWLComplementClass(unionCls);
        OWLAllValuesFrom restriction = owlModel.createOWLAllValuesFrom(property, complementClass);
        bCls.addSuperclass(restriction);
        assertEquals(1, ((Cls) bCls).getTemplateSlotAllowedClses(property).size());
        assertEquals(complementClass, ((Cls) bCls).getTemplateSlotAllowedClses(property).iterator().next());

        bCls.removeSuperclass(restriction);
        assertFalse(((Cls) bCls).hasDirectlyOverriddenTemplateFacet(property, valueTypeFacet));

        assertEquals(oldClsCount, owlModel.getClsCount());
    }


    public void testChangeAllFacets() {

        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("HappyPerson");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("OtherPerson");
        aCls.addSuperclass(personCls);
        bCls.addSuperclass(personCls);
        OWLProperty property = owlModel.createOWLObjectProperty("children");
        property.setRange(personCls);
        property.setDomain(personCls);
        int oldClsCount = owlModel.getClsCount();

        assertEquals(1, personCls.getSuperclassCount());
        ((Cls) personCls).setTemplateSlotValueType(property, ValueType.INSTANCE);
        ((Cls) personCls).setTemplateSlotAllowedClses(property, Collections.singleton(aCls));
        assertEquals(2, personCls.getSuperclassCount());
        OWLAllValuesFrom oldRestriction = (OWLAllValuesFrom) personCls.getSuperclasses(false).toArray()[1];
        assertEquals(aCls, oldRestriction.getFiller());

        OWLIntersectionClass i = owlModel.createOWLIntersectionClass(Arrays.asList(new Cls[]{bCls, aCls}));
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(aCls);
        ((Cls) personCls).setTemplateSlotAllowedClses(property, Arrays.asList(new Cls[]{complementCls, i}));
        Collection supers = personCls.getSuperclasses(false);
        assertEquals(2, supers.size());
        OWLAllValuesFrom unionRestriction = (OWLAllValuesFrom) supers.toArray()[1];
        assertTrue(unionRestriction.getFiller() instanceof OWLUnionClass);
        Collection operands = ((OWLUnionClass) unionRestriction.getFiller()).getOperands();
        assertTrue(operands.contains(complementCls));
        assertTrue(operands.contains(i));
        assertTrue(owlModel.containsFrame(complementCls.getName()));

        ((Cls) personCls).setTemplateSlotAllowedClses(property, Collections.singleton(complementCls));
        assertEquals(2, personCls.getSuperclassCount());
        OWLAllValuesFrom newRestriction = (OWLAllValuesFrom) personCls.getSuperclasses(false).toArray()[1];
        assertTrue(newRestriction.getFiller().equals(complementCls));

        ((Cls) personCls).setTemplateSlotAllowedClses(property, Collections.EMPTY_LIST);
        ((Cls) personCls).setTemplateFacetValues(property, owlModel.getFacet(Model.Facet.VALUE_TYPE), Collections.EMPTY_LIST);

        assertEquals(oldClsCount, owlModel.getClsCount());
    }


    public void testXMLLiteral() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Cls");
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("slot");
        property.addUnionDomainClass(c);
        assertEquals(ValueType.ANY, ((Cls) c).getTemplateSlotValueType(property));
        OWLAllValuesFrom allValuesFrom = owlModel.createOWLAllValuesFrom(property, owlModel.getRDFXMLLiteralType());
        c.addSuperclass(allValuesFrom);
        assertEquals(ValueType.STRING, ((Cls) c).getTemplateSlotValueType(property));
    }
}
