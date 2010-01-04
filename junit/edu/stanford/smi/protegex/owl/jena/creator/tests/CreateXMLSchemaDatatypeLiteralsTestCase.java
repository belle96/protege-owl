package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.datatypes.xsd.impl.XMLLiteralType;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateXMLSchemaDatatypeLiteralsTestCase extends AbstractJenaCreatorTestCase {

    public void testAnyURI() {
        final XSDDatatype anyURI = XSDDatatype.XSDanyURI;
        RDFSDatatype datatype = owlModel.getRDFSDatatypeByURI(anyURI.getURI());
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", datatype);
        slot.setDomainDefined(false);
        final String uri = "http://aldi.de";
        slot.setPropertyValue(slot, owlModel.createRDFSLiteral(uri, datatype));

        OntModel newModel = runJenaCreator();
        DatatypeProperty datatypeProperty = newModel.getDatatypeProperty(slot.getURI());
        assertEquals(anyURI.getURI(), datatypeProperty.getRange().getURI());
        RDFNode node = datatypeProperty.getPropertyValue(datatypeProperty);
        assertTrue(node.canAs(Literal.class));
        Literal literal = (Literal) node.as(Literal.class);
        assertEquals(anyURI, literal.getDatatype());
    }


    public void testBase64Binary() {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        property.setRange(owlModel.getXSDbase64Binary());
        byte[] values = new byte[]{1, 2, 3};
        RDFSLiteral literal = owlModel.createRDFSLiteral(values);
        assertEquals(owlModel.getXSDbase64Binary(), literal.getDatatype());
        property.setPropertyValue(property, literal);

        OntModel newModel = runJenaCreator();
        OntProperty ontProperty = newModel.getOntProperty(property.getURI());
        assertEquals(owlModel.getXSDbase64Binary().getURI(), ontProperty.getRange().getURI());
        Literal newLiteral = (Literal) ontProperty.getPropertyValue(ontProperty);
        assertEquals(owlModel.getXSDbase64Binary().getURI(), newLiteral.getDatatypeURI());
    }


    public void testXMLLiteralWithRangeDefinition() throws Exception {
        OWLDatatypeProperty datatypeProperty = owlModel.createOWLDatatypeProperty("property", owlModel.getRDFXMLLiteralType());
        datatypeProperty.setDomainDefined(false);
        String value = "<P>test</P>";
        datatypeProperty.addPropertyValue(datatypeProperty, owlModel.createRDFSLiteral(value, owlModel.getRDFXMLLiteralType()));

        OntModel newModel = runJenaCreator();
        DatatypeProperty ontProperty = newModel.getDatatypeProperty(datatypeProperty.getURI());
        assertEquals(XMLLiteralType.theXMLLiteralType.getURI(), ontProperty.getRange().getURI());
        assertSize(1, ontProperty.listProperties(ontProperty));
        RDFNode node = (RDFNode) ontProperty.getPropertyValue(ontProperty);
        Literal literal = (Literal) node.as(Literal.class);
        assertEquals(XMLLiteralType.theXMLLiteralType.getURI(), literal.getDatatypeURI());
        assertEquals(value, literal.getString());
    }


    public void testXMLLiteralWithoutRangeDefinition() throws Exception {
        OWLDatatypeProperty datatypeProperty = owlModel.createOWLDatatypeProperty("property");
        datatypeProperty.setDomainDefined(false);
        String value = "<P>test</P>";
        RDFSLiteral literal = owlModel.createRDFSLiteral(value, owlModel.getRDFXMLLiteralType());
        datatypeProperty.addPropertyValue(datatypeProperty, literal);

        OntModel newModel = runJenaCreator();
        DatatypeProperty ontProperty = newModel.getDatatypeProperty(datatypeProperty.getURI());
        assertSize(1, ontProperty.listProperties(ontProperty));
        RDFNode node = (RDFNode) ontProperty.getPropertyValue(ontProperty);
        Literal ontLiteral = (Literal) node.as(Literal.class);
        assertEquals(XMLLiteralType.theXMLLiteralType.getURI(), ontLiteral.getDatatypeURI());
        assertEquals(value, literal.getString());
    }
}
