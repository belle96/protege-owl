package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.datatypes.xsd.impl.XMLLiteralType;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateXMLSchemaDatatypeLiteralsTestCase extends AbstractProtege2JenaTestCase {

    public void testAnyURI() {
        final XSDDatatype anyURI = XSDDatatype.XSDanyURI;
        RDFSDatatype datatype = owlModel.getRDFSDatatypeByURI(anyURI.getURI());
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", datatype);
        slot.setDomainDefined(false);
        final String uri = "http://aldi.de";
        slot.setPropertyValue(slot, owlModel.createRDFSLiteral(uri, datatype));

        OntModel newModel = createOntModel();
        DatatypeProperty datatypeProperty = newModel.getDatatypeProperty(slot.getURI());
        assertEquals(anyURI.getURI(), datatypeProperty.getRange().getURI());
        RDFNode node = datatypeProperty.getPropertyValue(datatypeProperty);
        assertTrue(node.canAs(Literal.class));
        Literal literal = (Literal) node.as(Literal.class);
        assertEquals(anyURI, literal.getDatatype());
    }


    public void testXMLLiteral() {
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getRDFXMLLiteralType());
        slot.setDomainDefined(false);
        String value = "<P>test</P>";
        slot.addPropertyValue(slot, owlModel.createRDFSLiteral(value, owlModel.getRDFXMLLiteralType()));

        OntModel newModel = createOntModel();
        DatatypeProperty property = newModel.getDatatypeProperty(slot.getURI());
        assertEquals(XMLLiteralType.theXMLLiteralType.getURI(), property.getRange().getURI());
        assertSize(1, property.listProperties(property));
        RDFNode node = (RDFNode) property.getPropertyValue(property);
        Literal literal = (Literal) node.as(Literal.class);
        assertEquals(XMLLiteralType.theXMLLiteralType.getURI(), literal.getDatatypeURI());
        assertEquals(value, literal.getString());
    }
}
