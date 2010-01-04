package edu.stanford.smi.protegex.owl.ui.widget.tests;

import edu.stanford.smi.protege.widget.WidgetMapper;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.widget.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLWidgetMapperTestCase extends AbstractJenaTestCase {

    private String getDefaultWidgetClassName(RDFSNamedClass c, RDFProperty property) {
        return owlModel.getProject().getWidgetMapper().getDefaultWidgetClassName(c, property, null);
    }


    public void testDomainDefined() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        property.setDomainDefined(true);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        property.addUnionDomainClass(cls);
        assertNotNull(new OWLWidgetMapper(owlModel).getDefaultWidgetClassName(cls, property, null));
    }


    public void testNoDomainDefined() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        property.setDomainDefined(false);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        assertNull(new OWLWidgetMapper(owlModel).getDefaultWidgetClassName(cls, property, null));
    }


    public void testNoDomainDefinedAndFunctional() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        property.setDomainDefined(false);
        property.setFunctional(true);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        assertNull(new OWLWidgetMapper(owlModel).getDefaultWidgetClassName(cls, property, null));
    }


    public void testNoDomainDefinedButAllRestriction() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        property.setDomainDefined(false);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        cls.addSuperclass(owlModel.createOWLAllValuesFrom(property, cls));
        assertNotNull(new OWLWidgetMapper(owlModel).getDefaultWidgetClassName(cls, property, null));
    }


    public void testNoDomainDefinedButCardiRestriction() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        property.setDomainDefined(false);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        cls.addSuperclass(owlModel.createOWLCardinality(property, 1));
        assertNotNull(new OWLWidgetMapper(owlModel).getDefaultWidgetClassName(cls, property, null));
    }


    public void testNoDomainDefinedButMinCardiRestriction() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        property.setDomainDefined(false);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        cls.addSuperclass(owlModel.createOWLMinCardinality(property, 1));
        assertNotNull(new OWLWidgetMapper(owlModel).getDefaultWidgetClassName(cls, property, null));
    }


    public void testNoDomainDefinedButInheritedMinCardiRestriction() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        property.setDomainDefined(false);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLNamedClass subCls = owlModel.createOWLNamedSubclass("SubCls", cls);
        cls.addSuperclass(owlModel.createOWLMinCardinality(property, 1));
        assertNotNull(new OWLWidgetMapper(owlModel).getDefaultWidgetClassName(subCls, property, null));
        assertNull(new OWLWidgetMapper(owlModel).getDefaultWidgetClassName(owlModel.getOWLNamedClassClass(), property, null));
    }


    public void testNoDomainDefinedButSomeRestriction() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        property.setDomainDefined(false);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        cls.addSuperclass(owlModel.createOWLSomeValuesFrom(property, cls));
        assertNotNull(new OWLWidgetMapper(owlModel).getDefaultWidgetClassName(cls, property, null));
    }


    public void testNoDirectDomainButSuperproperty() {
        WidgetMapper mapper = owlModel.getProject().getWidgetMapper();
        RDFProperty superProperty = owlModel.createOWLDatatypeProperty("superProperty");
        RDFProperty subProperty = owlModel.createOWLDatatypeProperty("subProperty");
        subProperty.addSuperproperty(superProperty);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        superProperty.setDomain(cls);
        assertFalse(subProperty.isDomainDefined());
        assertTrue(subProperty.isDomainDefined(true));
        assertNotNull(mapper.getDefaultWidgetClassName(cls, superProperty, null));
        assertNotNull(mapper.getDefaultWidgetClassName(cls, subProperty, null));
    }


    public void testWidgetsOfRDFProperties() {
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        WidgetMapper mapper = owlModel.getProject().getWidgetMapper();
        RDFProperty property = owlModel.createRDFProperty("property");
        property.setDomain(cls);

        assertNull(property.getRange());
        assertEquals(MultiLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setFunctional(true);
        assertEquals(SingleLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setRange(owlModel.getXSDstring());
        assertEquals(SingleLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setRange(owlModel.getXSDint());
        assertEquals(SingleLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setRange(owlModel.getXSDfloat());
        assertEquals(SingleLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setRange(owlModel.getXSDdouble());
        assertEquals(SingleLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setRange(owlModel.getXSDdate());
        assertEquals(OWLDateWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setRange(owlModel.getXSDdateTime());
        assertEquals(OWLDateTimeWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setRange(owlModel.getXSDtime());
        assertEquals(OWLTimeWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setRange(owlModel.getXSDboolean());
        assertEquals(SingleLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setRange(cls);
        assertEquals(SingleResourceWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setFunctional(false);
        assertEquals(MultiResourceWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        property.setRange(owlModel.getXSDstring());
        assertEquals(MultiLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));
    }


    public void testWidgetsOfOWLDatatypeProperty() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        WidgetMapper mapper = owlModel.getProject().getWidgetMapper();
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");

        assertNull(property.getRange());
        assertEquals(null, mapper.getDefaultWidgetClassName(cls, property, null));
        cls.addSuperclass(owlModel.createOWLMaxCardinality(property, 1));

        OWLRestriction restriction = owlModel.createOWLAllValuesFrom(property, owlModel.getXSDstring());
        cls.addSuperclass(restriction);
        assertEquals(SingleLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        restriction.delete();
        restriction = owlModel.createOWLAllValuesFrom(property, owlModel.getXSDint());
        cls.addSuperclass(restriction);
        assertEquals(SingleLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        restriction.delete();
        restriction = owlModel.createOWLAllValuesFrom(property, owlModel.getXSDfloat());
        cls.addSuperclass(restriction);
        assertEquals(SingleLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        restriction.delete();
        restriction = owlModel.createOWLAllValuesFrom(property, owlModel.getXSDdouble());
        cls.addSuperclass(restriction);
        assertEquals(SingleLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        restriction.delete();
        restriction = owlModel.createOWLAllValuesFrom(property, owlModel.getXSDdate());
        cls.addSuperclass(restriction);
        assertEquals(OWLDateWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        restriction.delete();
        restriction = owlModel.createOWLAllValuesFrom(property, owlModel.getXSDdateTime());
        cls.addSuperclass(restriction);
        assertEquals(OWLDateTimeWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        restriction.delete();
        restriction = owlModel.createOWLAllValuesFrom(property, owlModel.getXSDtime());
        cls.addSuperclass(restriction);
        assertEquals(OWLTimeWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));

        restriction.delete();
        restriction = owlModel.createOWLAllValuesFrom(property, owlModel.getXSDboolean());
        cls.addSuperclass(restriction);
        assertEquals(SingleLiteralWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));
    }


    public void testOWLDataRangeProperty() {
        WidgetMapper mapper = owlModel.getProject().getWidgetMapper();
        RDFSLiteral[] literals = new RDFSLiteral[]{
                owlModel.createRDFSLiteral("1", owlModel.getXSDint()),
                owlModel.createRDFSLiteral("2", owlModel.getXSDint())
        };
        OWLDataRange dataRange = owlModel.createOWLDataRange(literals);
        RDFProperty property = owlModel.createOWLDatatypeProperty("property");
        property.setRange(dataRange);
        property.setFunctional(true);
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        property.setDomain(cls);
        assertEquals(DataRangeFieldWidget.class.getName(), mapper.getDefaultWidgetClassName(cls, property, null));
    }


    public void testOWLMaxCardinality0() {
        WidgetMapper mapper = owlModel.getProject().getWidgetMapper();
        OWLNamedClass superclass = owlModel.createOWLNamedClass("Super");
        OWLNamedClass subclass = owlModel.createOWLNamedSubclass("Sub", superclass);
        OWLNamedClass leafclass = owlModel.createOWLNamedSubclass("Leaf", subclass);
        RDFProperty property = owlModel.createRDFProperty("property");
        property.setDomain(superclass);
        assertNotNull(mapper.getDefaultWidgetClassName(superclass, property, null));
        assertNotNull(mapper.getDefaultWidgetClassName(subclass, property, null));
        subclass.addSuperclass(owlModel.createOWLMaxCardinality(property, 0));
        assertNull(mapper.getDefaultWidgetClassName(subclass, property, null));
        assertNull(mapper.getDefaultWidgetClassName(leafclass, property, null));
    }


    public void testEnumeratedDatatype() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        property.setRange(owlModel.createOWLDataRange());
        property.setDomain(c);
        assertEquals(MultiLiteralWidget.class.getName(), getDefaultWidgetClassName(c, property));
    }
}
