package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadOWLEnumeratedClassTestCase extends AbstractJenaTestCase {

    public void testLoadEnumerationWithClass() throws Exception {
        loadRemoteOntology("enum-with-class.owl");
        OWLNamedClass cls = owlModel.getOWLNamedClass("Cls");
        OWLNamedClass enumCls = owlModel.getOWLNamedClass("Enum");
        OWLObjectProperty slot = owlModel.getOWLObjectProperty("slot");
        assertNotNull(cls);
        assertNotNull(enumCls);
        assertNotNull(slot);
        OWLEnumeratedClass e = (OWLEnumeratedClass) enumCls.getDefinition();
        assertSize(2, e.getOneOf());
        assertContains(cls, e.getOneOf());
        assertContains(slot, e.getOneOf());
    }


    public void testLoadInlineEnumeratedClass() throws Exception {
        loadRemoteOntology("colors.owl");
        OWLNamedClass c = owlModel.getOWLNamedClass("TrafficLightColors");
        assertNotNull(c);
        assertTrue(c.getDefinition() instanceof OWLEnumeratedClass);
    }
}
