package edu.stanford.smi.protegex.owl.ui.widget.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.widget.OWLWidgetMetadata;
import edu.stanford.smi.protegex.owl.ui.widget.SingleResourceWidgetMetadata;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SingleResourceWidgetMetadataTestCase extends AbstractJenaTestCase {

    public void testRangelessProperty() {
        RDFProperty property = owlModel.createRDFProperty("property");
        OWLWidgetMetadata metadata = new SingleResourceWidgetMetadata();
        assertTrue(metadata.getSuitability(owlThing, property) == OWLWidgetMetadata.NOT_SUITABLE);
    }


    public void testRangelessFunctionalProperty() {
        RDFProperty property = owlModel.createRDFProperty("property");
        OWLWidgetMetadata metadata = new SingleResourceWidgetMetadata();
        property.setFunctional(true);
        assertTrue(metadata.getSuitability(owlThing, property) == OWLWidgetMetadata.NOT_SUITABLE);
    }


    public void testFunctionalObjectProperty() {
        RDFProperty property = owlModel.createRDFProperty("property");
        OWLWidgetMetadata metadata = new SingleResourceWidgetMetadata();
        property.setFunctional(true);
        property.setRange(owlThing);
        assertTrue(metadata.getSuitability(owlThing, property) == OWLWidgetMetadata.DEFAULT);
    }


    public void testMaxCardinalityObjectProperty() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        cls.addSuperclass(owlModel.createOWLMaxCardinality(property, 1));
        OWLWidgetMetadata metadata = new SingleResourceWidgetMetadata();
        assertTrue(metadata.getSuitability(cls, property) == OWLWidgetMetadata.DEFAULT);
    }
}
