package edu.stanford.smi.protegex.owl.ui.widget.tests;

import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.widget.MultiResourceWidgetMetadata;
import edu.stanford.smi.protegex.owl.ui.widget.OWLWidgetMetadata;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class MultiResourceWidgetMetadataTestCase extends AbstractJenaTestCase {

    public void testRangelessRDFProperty() {
        RDFProperty property = owlModel.createRDFProperty("property");
        OWLWidgetMetadata metadata = new MultiResourceWidgetMetadata();
        assertTrue(metadata.getSuitability(owlThing, property) > OWLWidgetMetadata.NOT_SUITABLE);
    }


    public void testOWLObjectPropertyWithRange() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        property.setRange(owlThing);
        OWLWidgetMetadata metadata = new MultiResourceWidgetMetadata();
        assertEquals(OWLWidgetMetadata.DEFAULT, metadata.getSuitability(owlThing, property));
    }
}
