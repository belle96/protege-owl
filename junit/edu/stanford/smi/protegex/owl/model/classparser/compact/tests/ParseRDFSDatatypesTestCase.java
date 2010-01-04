package edu.stanford.smi.protegex.owl.model.classparser.compact.tests;

import edu.stanford.smi.protegex.owl.model.OWLAllValuesFrom;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.compact.CompactOWLClassParser;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ParseRDFSDatatypesTestCase extends AbstractJenaTestCase {

    private OWLClassParser parser = new CompactOWLClassParser();


    public void testParseClosedIntInterval() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        owlModel.createOWLDatatypeProperty("hasAge");
        String expression = "hasAge * int[18,24]";
        RDFSClass cls = parser.parseClass(owlModel, expression);
        assertTrue(cls instanceof OWLAllValuesFrom);
        OWLAllValuesFrom allValuesFrom = (OWLAllValuesFrom) cls;
        assertEquals("int[18,24]", allValuesFrom.getFiller().getBrowserText());
    }


    public void testParseOpenIntInterval() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        owlModel.createOWLDatatypeProperty("hasAge");
        String expression = "hasAge * int(18,..)";
        RDFSClass cls = parser.parseClass(owlModel, expression);
        assertTrue(cls instanceof OWLAllValuesFrom);
        OWLAllValuesFrom allValuesFrom = (OWLAllValuesFrom) cls;
        assertEquals("int(18,..)", allValuesFrom.getFiller().getBrowserText());
    }


    public void testParseOpenIntIntervalAbbrev() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        owlModel.createOWLDatatypeProperty("hasAge");
        String expression = "hasAge * int(18,..)";
        RDFSClass cls = parser.parseClass(owlModel, expression);
        assertTrue(cls instanceof OWLAllValuesFrom);
        OWLAllValuesFrom allValuesFrom = (OWLAllValuesFrom) cls;
        assertEquals("int(18,..)", allValuesFrom.getFiller().getBrowserText());
    }
}
