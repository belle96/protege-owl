package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddRDFSLiteralTestCase extends AbstractJenaTestCase {

    public void testAddStringValue() {
        RDFProperty property = owlModel.createRDFProperty("property");
        property.addPropertyValue(property, owlModel.createRDFSLiteral("Value", owlModel.getXSDstring()));
        assertSize(1, property.getPropertyValues(property));
        assertEquals("Value", property.getPropertyValue(property));
        assertContains("Value", property.getPropertyValues(property));
    }


    public void testAddBooleanValue() {
        RDFProperty property = owlModel.createRDFProperty("property");
        property.addPropertyValue(property, owlModel.createRDFSLiteral("" + Boolean.TRUE, owlModel.getXSDboolean()));
        assertSize(1, property.getPropertyValues(property));
        assertEquals(Boolean.TRUE, property.getPropertyValue(property));
        assertContains(Boolean.TRUE, property.getPropertyValues(property));
    }
}
