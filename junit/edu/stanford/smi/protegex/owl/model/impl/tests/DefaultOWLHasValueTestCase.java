package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLHasValue;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLHasValueTestCase extends AbstractJenaTestCase {

    public void testAssignStringFiller() throws Exception {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        OWLHasValue restriction = owlModel.createOWLHasValue();
        restriction.setOnProperty(property);
        restriction.setFillerText("Holger war hier");
    }


    public void testAssignIntFiller() throws Exception {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        OWLHasValue restriction = owlModel.createOWLHasValue();
        restriction.setOnProperty(property);
        restriction.setFillerText("42");
        assertEquals(new Integer(42), restriction.getHasValue());
    }


    public void testAssignFloatFiller() throws Exception {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        OWLHasValue restriction = owlModel.createOWLHasValue();
        restriction.setOnProperty(property);
        restriction.setFillerText("4.2");
        assertEquals(new Float(4.2), restriction.getHasValue());
    }
}
