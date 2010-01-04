package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateImportTestCase extends AbstractProtege2JenaTestCase {

    public void testCreateImport() {
        String uri = "http://protege.stanford.edu/plugins/owl/owl-library/koala.owl";
        owlModel.getDefaultOWLOntology().addImports(uri);
        OntModel newModel = createOntModel();
        Ontology ontology = (Ontology) newModel.listOntologies().next();
        assertSize(1, ontology.listImports());
        assertEquals(uri, ontology.listImports().next().toString());
    }


    public void testCyclicImport() throws Exception {
        loadRemoteOntology("cyclicFood.owl");
        OntModel newModel = createOntModel();
        OntClass ontClass = newModel.getOntClass("http://protege.stanford.edu/plugins/owl/testdata/cyclicFood.owl#Food");
        assertTrue(newModel.getBaseModel().contains(ontClass, RDF.type, OWL.Class));
    }
}
