package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadOWLDeprecatedPropertyTestCase extends AbstractJenaTestCase {


    public void testLoadDeprecatedProperty() throws Exception {
        loadRemoteOntology("deprecated.owl");
        assertNotNull(owlModel.getOWLObjectProperty("drives"));
        RDFProperty property = (RDFProperty) owlModel.getRDFProperty("hasDriver");
        assertNotNull(property);
        // assertFalse(property instanceof OWLObjectProperty);
        assertTrue(property.isDeprecated());
    }
}
