package edu.stanford.smi.protegex.owl.model.classdisplay.manchester.tests;

import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.classdisplay.manchester.ManchesterOWLClassDisplay;
import edu.stanford.smi.protegex.owl.model.classdisplay.tests.AbstractOWLClassRendererTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ManchesterOWLClassDisplayTestCase extends AbstractOWLClassRendererTestCase {


    public ManchesterOWLClassDisplayTestCase() {
        super(new ManchesterOWLClassDisplay());
    }


    // !(Class | (property = 1))
    public void testComplexExpression() {
        OWLComplementClass complement = getComplexComplement();
        assertEquals("not (Class or (property exactly 1))", getDisplayText(complement));
    }


    public void testComplementOfNamedClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLComplementClass complement = owlModel.createOWLComplementClass(cls);
        assertEquals("not Class", getDisplayText(complement));
    }
}
