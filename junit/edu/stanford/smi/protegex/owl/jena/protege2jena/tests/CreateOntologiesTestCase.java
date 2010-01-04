package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.vocabulary.OWL;

import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateOntologiesTestCase extends AbstractProtege2JenaTestCase {

    public void testDefaultOntology() {
        OntModel newModel = createOntModel();
        assertSize(1, newModel.listOntologies());
        Ontology ontology = (Ontology) newModel.listOntologies().next();
        assertEquals(newModel.getNsPrefixURI(""), ontology.getURI() + "#");
    }


    public void testOntologyProperty() {
        String priorVersion = "http://old.de#";
        owlModel.getDefaultOWLOntology().addPriorVersion(priorVersion);
        OntModel newModel = createOntModel();
        assertSize(1, newModel.listOntologies());
        Ontology ontology = (Ontology) newModel.listOntologies().next();
        Literal value = (Literal) ontology.getPropertyValue(OWL.priorVersion);
        assertEquals(priorVersion, value.getString());
    }


    public void testOntologyWithNiceNamespace() {
        owlModel.createOWLNamedClass("Cls");
        String namespace = "http://aldi.de/ont";
        OWLUtil.renameOntology(owlModel, owlModel.getDefaultOWLOntology(), namespace);
        OntModel newModel = createOntModel();
        assertSize(1, newModel.listOntologies());
        Ontology ontology = (Ontology) newModel.listOntologies().next();
        assertEquals(namespace, ontology.getURI());
    }


    public void testOntologyWithUglyNamespace() throws Exception {
        owlModel.getDefaultOWLOntology().addImports(getRemoteOntologyRoot() + "travel.owl");
        owlModel.createOWLNamedClass("Cls");
        String namespace = "http://aldi.de/ont/";
        OWLUtil.renameOntology(owlModel, owlModel.getDefaultOWLOntology(), namespace);
        owlModel = reload(owlModel);
        OntModel newModel = createOntModel();
        assertSize(2, newModel.listOntologies());
        Ontology ontology = (Ontology) newModel.getOntology(namespace);
        assertEquals(namespace, ontology.getURI());
        assertSize(1, ontology.listImports());                                                           // the ontology import is broken but
        assertNotNull(owlModel.getOWLNamedClass("http://www.owl-ontologies.com/travel.owl#Sunbathing")); // the data has arrived
    }
}
