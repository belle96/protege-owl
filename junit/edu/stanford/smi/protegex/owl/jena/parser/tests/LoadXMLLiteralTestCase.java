package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadXMLLiteralTestCase extends AbstractJenaTestCase {


    public void testLoadXMLLiteral() throws Exception {
        loadRemoteOntology("XMLLiteralValue.owl");
        OWLDatatypeProperty slot = (OWLDatatypeProperty) owlModel.getOWLDatatypeProperty("anno");
        assertEquals(owlModel.getRDFXMLLiteralType(), slot.getRange());
        assertTrue(XMLSchemaDatatypes.isXMLLiteralSlot(slot));
        assertSize(1, slot.getPropertyValues(slot));
        final Object value = slot.getPropertyValue(slot);
        assertTrue(value instanceof RDFSLiteral);
        RDFSLiteral literal = (RDFSLiteral) value;
        assertEquals(owlModel.getRDFXMLLiteralType(), literal.getDatatype());
        // assertEquals("<P>Value</P>", value);
    }
}
