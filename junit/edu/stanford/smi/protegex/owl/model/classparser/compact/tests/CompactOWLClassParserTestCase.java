package edu.stanford.smi.protegex.owl.model.classparser.compact.tests;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.compact.CompactOWLClassParser;
import edu.stanford.smi.protegex.owl.tests.AbstractOWLModelTestCase;

import java.util.ArrayList;
import java.util.Collection;

public class CompactOWLClassParserTestCase extends AbstractOWLModelTestCase {

    private OWLClassParser parser = new CompactOWLClassParser();


    public void testParseComplementClass() throws Exception {
        owlModel.createOWLNamedClass("Person");
        String expression = "!Person";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLComplementClass);
        OWLComplementClass complementCls = (OWLComplementClass) aClass;
        Cls complement = complementCls.getComplement();
        assertTrue(complement instanceof OWLNamedClass);
        assertEquals(complement, owlModel.getRDFSNamedClass("Person"));
    }


    public void testParseIntersection() throws Exception {
        RDFSClass richPerson = owlModel.createOWLNamedClass("RichPerson");
        RDFSClass parent = owlModel.createOWLNamedClass("Parent");
        String expression = "RichPerson & Parent";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLIntersectionClass);
        OWLIntersectionClass intersectionCls = (OWLIntersectionClass) aClass;
        Collection clses = intersectionCls.getOperands();
        assertEquals(clses.size(), 2);
        assertTrue(clses.contains(richPerson));
        assertTrue(clses.contains(parent));
    }


    public void testParseLongIntersection() throws Exception {
        owlModel.createOWLNamedClass("RichPerson");
        owlModel.createOWLNamedClass("Parent");
        owlModel.createOWLObjectProperty("children");
        String expression = "RichPerson & Parent & (children * RichPerson)";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLIntersectionClass);
        OWLIntersectionClass intersectionCls = (OWLIntersectionClass) aClass;
        Collection clses = new ArrayList(intersectionCls.getOperands());
        assertEquals(clses.size(), 3);
        clses.remove(owlModel.getRDFSNamedClass("RichPerson"));
        clses.remove(owlModel.getRDFSNamedClass("Parent"));
        assertEquals(clses.size(), 1);
        Cls all = (Cls) clses.iterator().next();
        assertTrue(all instanceof OWLAllValuesFrom);
    }


    public void testParseNamedClass() throws Exception {
        owlModel.createOWLNamedClass("Person");
        String expression = "Person";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLNamedClass);
    }


    public void testParseUnion() throws Exception {
        owlModel.createOWLNamedClass("RichPerson");
        owlModel.createOWLNamedClass("Parent");
        String expression = "RichPerson | Parent";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLUnionClass);
        OWLUnionClass unionCls = (OWLUnionClass) aClass;
        Collection clses = unionCls.getOperands();
        assertEquals(clses.size(), 2);
        assertTrue(clses.contains(owlModel.getRDFSNamedClass("RichPerson")));
        assertTrue(clses.contains(owlModel.getRDFSNamedClass("Parent")));
    }
}
