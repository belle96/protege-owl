package edu.stanford.smi.protegex.owl.jena.parser.tests;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadOWLFunctionalPropertyTestCase extends AbstractJenaTestCase {

    public void testLoadFunctionalProperty() throws Exception {
        RDFProperty property = owlModel.createRDFProperty("property");
        property.setFunctional(true);
        JenaOWLModel newModel = (JenaOWLModel) reload(owlModel);
        OntModel newOntModel = newModel.getOntModel();
        Jena.dumpRDF(newOntModel);
        RDFProperty newProperty = newModel.getRDFProperty(property.getName());
        assertTrue(newProperty.isFunctional());
    }


    public void testLoadFunctionalAnnotationProperty() throws Exception {
        loadRemoteOntology("functional.owl");
        OWLProperty property = owlModel.getOWLProperty("functionalAnnotationProperty");
        assertTrue(property.isAnnotationProperty());
        assertTrue(property.isFunctional());
    }


    public void testLoadFunctionalDatatypeProperty() throws Exception {
        loadRemoteOntology("functional.owl");
        OWLProperty property = owlModel.getOWLProperty("functionalProperty");
        assertFalse(property.isAnnotationProperty());
        assertTrue(property.isFunctional());
    }
}
