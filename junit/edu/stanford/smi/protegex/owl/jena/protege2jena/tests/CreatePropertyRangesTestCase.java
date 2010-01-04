package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.Resource;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreatePropertyRangesTestCase extends AbstractProtege2JenaTestCase {

    public void testDataRangeDefault() {
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", new RDFSLiteral[]{
                owlModel.createRDFSLiteral("A"),
                owlModel.createRDFSLiteral("B")
        });
        OntModel newModel = createOntModel();
        DatatypeProperty datatypeProperty = newModel.getDatatypeProperty(slot.getURI());
        OntResource range = datatypeProperty.getRange();
        assertTrue(range.canAs(DataRange.class));
        DataRange dataRange = (DataRange) range.as(DataRange.class);
        Iterator it = dataRange.listOneOf();
        assertEquals(newModel.createTypedLiteral("A"), it.next());
        assertEquals(newModel.createTypedLiteral("B"), it.next());
        assertFalse(it.hasNext());
    }


    public void testDataRangeInt() {
        XSDDatatype xsd = XSDDatatype.XSDpositiveInteger;
        RDFSDatatype positiveInteger = owlModel.getRDFSDatatypeByURI(xsd.getURI());
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", new RDFSLiteral[]{
                owlModel.createRDFSLiteral("42", positiveInteger),
                owlModel.createRDFSLiteral("43", positiveInteger),
        });
        OntModel newModel = createOntModel();
        DatatypeProperty datatypeProperty = newModel.getDatatypeProperty(slot.getURI());
        OntResource range = datatypeProperty.getRange();
        assertTrue(range.canAs(DataRange.class));
        DataRange dataRange = (DataRange) range.as(DataRange.class);
        Iterator it = dataRange.listOneOf();
        assertEquals(newModel.createTypedLiteral("42", xsd), it.next());
        assertEquals(newModel.createTypedLiteral("43", xsd), it.next());
        assertFalse(it.hasNext());
    }


    public void testDatatypeRanges() {
        OWLProperty booleanProperty = owlModel.createOWLDatatypeProperty("booleanProperty", owlModel.getXSDboolean());
        OWLProperty floatProperty = owlModel.createOWLDatatypeProperty("floatProperty", owlModel.getXSDfloat());
        OWLProperty intProperty = owlModel.createOWLDatatypeProperty("intProperty", owlModel.getXSDint());
        OWLProperty stringProperty = owlModel.createOWLDatatypeProperty("stringProperty", owlModel.getXSDstring());
        OWLDatatypeProperty dateProperty = owlModel.createOWLDatatypeProperty("dateProperty", owlModel.getRDFSDatatypeByURI(XSDDatatype.XSDdate.getURI()));
        OntModel newModel = createOntModel();
        assertEquals(XMLSchemaDatatypes.getValueTypeURI(ValueType.BOOLEAN),
                newModel.getDatatypeProperty(booleanProperty.getURI()).getRange().getURI());
        assertEquals(XMLSchemaDatatypes.getValueTypeURI(ValueType.FLOAT),
                newModel.getDatatypeProperty(floatProperty.getURI()).getRange().getURI());
        assertEquals(XMLSchemaDatatypes.getValueTypeURI(ValueType.INTEGER),
                newModel.getDatatypeProperty(intProperty.getURI()).getRange().getURI());
        assertEquals(XMLSchemaDatatypes.getValueTypeURI(ValueType.STRING),
                newModel.getDatatypeProperty(stringProperty.getURI()).getRange().getURI());
        assertEquals(XSDDatatype.XSDdate.getURI(),
                newModel.getDatatypeProperty(dateProperty.getURI()).getRange().getURI());
    }


    public void testObjectPropertyRangeMultiple() {
        OWLNamedClass clsA = owlModel.createOWLNamedClass("ClsA");
        OWLNamedClass clsB = owlModel.createOWLNamedClass("ClsB");
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        slot.setUnionRangeClasses(Arrays.asList(new Cls[]{clsA, clsB}));
        OntModel newModel = createOntModel();
        ObjectProperty property = newModel.getObjectProperty(slot.getURI());
        Resource range = property.getRange();
        assertTrue(range.canAs(UnionClass.class));
        UnionClass unionClass = (UnionClass) range.as(UnionClass.class);
        Iterator it = unionClass.listOperands();
        assertEquals(newModel.getResource(clsA.getURI()), it.next());
        assertEquals(newModel.getResource(clsB.getURI()), it.next());
        assertFalse(it.hasNext());
    }


    public void testObjectPropertyRangeSingle() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        slot.setUnionRangeClasses(Collections.singleton(cls));
        OntModel newModel = createOntModel();
        ObjectProperty property = newModel.getObjectProperty(slot.getURI());
        assertEquals(newModel.getResource(cls.getURI()), property.getRange());
    }


    public void testRDFPropertyRangeBoolean() {
        RDFProperty slot = owlModel.createRDFProperty("slot");
        slot.setRange(owlModel.getXSDboolean());
        OntModel ontModel = createOntModel();
        OntProperty property = ontModel.getOntProperty(slot.getURI());
        OntResource range = property.getRange();
        assertEquals(XMLSchemaDatatypes.getDefaultXSDDatatype(ValueType.BOOLEAN).getURI(),
                range.getURI());
    }


    public void testRDFPropertyRangeInstance() {
        RDFSNamedClass aClass = owlModel.createRDFSNamedClass("Cls");
        RDFProperty rdfProperty = owlModel.createRDFProperty("rdfProperty");
        rdfProperty.setRange(aClass);
        OntModel ontModel = createOntModel();
        OntProperty ontProperty = ontModel.getOntProperty(rdfProperty.getURI());
        assertSize(1, ontProperty.listRange());
        assertEquals(ontModel.getOntClass(aClass.getURI()), ontProperty.getRange());
    }


    public void testSubslotRange() {
        final Slot valueTypeSlot = owlModel.getSlot(Model.Slot.VALUE_TYPE);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty superSlot = owlModel.createOWLObjectProperty("superSlot");
        OWLObjectProperty subSlot = (OWLObjectProperty) owlModel.createSubproperty("subSlot", superSlot);
        superSlot.setUnionRangeClasses(Collections.singleton(cls));
        assertSize(1, subSlot.getUnionRangeClasses());
        assertNull(((Slot) subSlot).getDirectOwnSlotValue(valueTypeSlot));

        OntModel newModel = createOntModel();
        OntProperty superProperty = newModel.getOntProperty(superSlot.getURI());
        assertNotNull(superProperty.getRange());
        OntProperty subProperty = newModel.getOntProperty(subSlot.getURI());
        assertNull(subProperty.getRange());
    }
}
