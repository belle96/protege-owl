package edu.stanford.smi.protegex.owl.jena.parser.tests;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadXMLSchemaDatatypesTestCase extends AbstractJenaTestCase {


    public void testLoadDateTime() throws Exception {
        String t = "2004-12-21T11:12:13";
        loadRemoteOntology("xsdDateTime.owl");
        RDFProperty property = owlModel.getRDFProperty("dateTime");
        RDFIndividual instance = owlModel.getRDFIndividual("Instance");
        RDFSLiteral literal = (RDFSLiteral) instance.getPropertyValue(property);
        assertEquals(XSDDatatype.XSDdateTime.getURI(), literal.getDatatype().getURI());
        assertEquals(t, literal.getString());
    }


    public void testLoadTime() throws Exception {
        String t = "01:54:59";
        loadRemoteOntology("xsdTime.owl");
        RDFProperty property = owlModel.getRDFProperty("time");
        RDFIndividual instance = owlModel.getRDFIndividual("Instance");
        RDFSLiteral literal = (RDFSLiteral) instance.getPropertyValue(property);
        assertEquals(XSDDatatype.XSDtime.getURI(), literal.getDatatype().getURI());
        assertEquals(t, literal.getString());
    }


    public void testLoadUnsupportedXMLSchemaDatatypes() throws Exception {
        loadRemoteOntology("xml-schema-datatypes.owl");
    }
}
