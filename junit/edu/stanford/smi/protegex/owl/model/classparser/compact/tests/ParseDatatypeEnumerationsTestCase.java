package edu.stanford.smi.protegex.owl.model.classparser.compact.tests;

import edu.stanford.smi.protegex.owl.model.OWLAllValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLDataRange;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.compact.CompactOWLClassParser;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ParseDatatypeEnumerationsTestCase extends AbstractJenaTestCase {

    private OWLClassParser parser = new CompactOWLClassParser();


    public void testFloatEnumeration() throws Exception {
        owlModel.createOWLDatatypeProperty("property", owlModel.getXSDfloat());
        OWLAllValuesFrom restriction = (OWLAllValuesFrom) parser.parseClass(owlModel,
                "property * owl:oneOf{1.1 2.2}");
        RDFResource filler = restriction.getFiller();
        assertTrue(filler instanceof OWLDataRange);
        OWLDataRange dataRange = (OWLDataRange) filler;
        RDFList oneOf = dataRange.getOneOf();
        assertNotNull(oneOf);
        assertSize(2, oneOf.getValues());
        assertContains(new Float(1.1), oneOf.getValues());
        assertContains(new Float(2.2), oneOf.getValues());
        assertEquals("owl:oneOf{1.1 2.2}", restriction.getFillerText());
    }


    public void testIntEnumeration() throws Exception {
        owlModel.createOWLDatatypeProperty("property", owlModel.getXSDint());
        OWLAllValuesFrom restriction = (OWLAllValuesFrom) parser.parseClass(owlModel,
                "property * owl:oneOf{1 2}");
        OWLDataRange dataRange = (OWLDataRange) restriction.getFiller();
        RDFList oneOf = dataRange.getOneOf();
        assertSize(2, oneOf.getValues());
        assertContains(new Integer(1), oneOf.getValues());
        assertContains(new Integer(2), oneOf.getValues());
        assertEquals("owl:oneOf{1 2}", restriction.getFillerText());
    }


    public void testStringEnumeration() throws Exception {
        owlModel.createOWLDatatypeProperty("property", owlModel.getXSDstring());
        OWLAllValuesFrom restriction = (OWLAllValuesFrom) parser.parseClass(owlModel,
                "property * owl:oneOf{\"A\" \"B\"}");
        OWLDataRange dataRange = (OWLDataRange) restriction.getFiller();
        RDFList oneOf = dataRange.getOneOf();
        assertSize(2, oneOf.getValues());
        assertContains("A", oneOf.getValues());
        assertContains("B", oneOf.getValues());
        assertEquals("owl:oneOf{\"A\" \"B\"}", restriction.getFillerText());
    }
}
