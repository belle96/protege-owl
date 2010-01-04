package edu.stanford.smi.protegex.owl.model.classdisplay.compact.tests;

import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.classdisplay.compact.CompactOWLClassDisplay;
import edu.stanford.smi.protegex.owl.model.classdisplay.tests.AbstractOWLClassRendererTestCase;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLComplementClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLUnionClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CompactOWLClassDisplayTestCase extends AbstractOWLClassRendererTestCase {


    public CompactOWLClassDisplayTestCase() {
        super(new CompactOWLClassDisplay());
    }


    public void testOWLNamedClass() {
        String name = "Person";
        OWLNamedClass cls = owlModel.createOWLNamedClass(name);
        assertEquals(name, getDisplayText(cls));
    }


    public void testComplexComplement() {
        OWLComplementClass complement = getComplexComplement();
        String str = "" + DefaultOWLComplementClass.OPERATOR + "(Class ";
        str += DefaultOWLUnionClass.OPERATOR + " (property = 1))";
        assertEquals(str, getDisplayText(complement));
    }


    public void testSomeValuesFromWithNestedUnion() {
        RDFSClass cls = getSomeValuesFromWithNestedUnion();
        assertEquals("property " + DefaultOWLSomeValuesFrom.OPERATOR +
                " (Class " + DefaultOWLUnionClass.OPERATOR + " " +
                DefaultOWLComplementClass.OPERATOR + "Class)",
                getDisplayText(cls));
    }
}
