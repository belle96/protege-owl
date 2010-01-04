package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.XSPNames;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSDatatype;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Map;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultRDFSDatatypeTestCase extends AbstractJenaTestCase {

    public void testIsNumericDatatype() {
        assertTrue(owlModel.getXSDfloat().isNumericDatatype());
        assertTrue(owlModel.getXSDint().isNumericDatatype());
        assertTrue(owlModel.getXSDinteger().isNumericDatatype());
        assertTrue(owlModel.getXSDdecimal().isNumericDatatype());
        assertTrue(owlModel.getXSDdouble().isNumericDatatype());
        assertTrue(owlModel.getXSDlong().isNumericDatatype());
        assertTrue(owlModel.getXSDshort().isNumericDatatype());
        assertFalse(owlModel.getXSDboolean().isNumericDatatype());
        assertFalse(owlModel.getXSDstring().isNumericDatatype());
    }

    /*public void testGetBaseDatatype() throws Exception {
       loadRemoteOntologyWithProtegeMetadataOntology();
       RDFSNamedClass metaclass = owlModel.getRDFSDatatypeClass();
       RDFSDatatype datatype = (RDFSDatatype) metaclass.createInstance("intOver18");
       assertEquals(datatype.getName(), datatype.getBrowserText());
       RDFProperty property = owlModel.getRDFProperty(DefaultRDFSDatatype.SIMPLE_TYPE_LITERAL_PROPERTY);
       assertNull(datatype.getBaseDatatype());
       String literal =
               "     <xsd:simpleType name=\"adultAge\">\n" +
               "       <xsd:restriction base=\"xsd:integer\">\n" +
               "        <xsd:minInclusive value=\"18\"/>\n" +
               "       </xsd:restriction>\n" +
               "     </xsd:simpleType> ";
       datatype.setPropertyValue(property, literal);
       assertEquals(owlModel.getXSDinteger(), datatype.getBaseDatatype());
       assertEquals(new Integer(18), datatype.getMinInclusive());
       assertEquals("xsd:integer[18,..]", datatype.getBrowserText());
   } */


    public void testGetBaseDatatype() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        RDFSDatatype datatype = owlModel.createRDFSDatatype("intOver18");
        RDFProperty subDatatypeOfProperty = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_BASE);
        datatype.setPropertyValue(subDatatypeOfProperty, owlModel.getXSDint());
        assertEquals(owlModel.getXSDint(), datatype.getBaseDatatype());
    }


    public void testUserDefinedIntType() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        String name = owlModel.getNextAnonymousResourceName();
        RDFSDatatype datatype = owlModel.createRDFSDatatype(name);
        RDFProperty subDatatypeOfProperty = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_BASE);
        datatype.setPropertyValue(subDatatypeOfProperty, owlModel.getXSDint());
        RDFProperty minInclusiveProperty = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_MIN_INCLUSIVE);
        datatype.setPropertyValue(minInclusiveProperty, new Integer(18));
        RDFProperty maxInclusiveProperty = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_MAX_INCLUSIVE);
        datatype.setPropertyValue(maxInclusiveProperty, new Integer(24));
        assertEquals(owlModel.createRDFSLiteral("24", owlModel.getXSDint()), datatype.getMaxInclusive());
        RDFProperty property = owlModel.createOWLDatatypeProperty("property");
        property.setRange(datatype);
        assertEquals("int[18,24]", datatype.getBrowserText());
    }


    public void testParseClosedInterval() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        RDFProperty subDatatypeOfProperty = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_BASE);
        RDFProperty minInclusiveProperty = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_MIN_INCLUSIVE);
        RDFProperty maxInclusiveProperty = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_MAX_INCLUSIVE);
        String expression = "xsd:int[18,24]";
        Map map = DefaultRDFSDatatype.parse(owlModel, expression);
        assertEquals(3, map.size());
        assertEquals(owlModel.getXSDint(), map.get(subDatatypeOfProperty));
        assertEquals(owlModel.createRDFSLiteral("18", owlModel.getXSDint()), map.get(minInclusiveProperty));
        assertEquals(owlModel.createRDFSLiteral("24", owlModel.getXSDint()), map.get(maxInclusiveProperty));
    }


    public void testParseOpenedInterval() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        RDFProperty subDatatypeOfProperty = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_BASE);
        RDFProperty maxExclusiveProperty = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_MAX_EXCLUSIVE);
        String expression = "xsd:int(..,24)";
        Map map = DefaultRDFSDatatype.parse(owlModel, expression);
        assertEquals(2, map.size());
        assertEquals(owlModel.getXSDint(), map.get(subDatatypeOfProperty));
        assertEquals(owlModel.createRDFSLiteral("24", owlModel.getXSDint()), map.get(maxExclusiveProperty));
    }
}
