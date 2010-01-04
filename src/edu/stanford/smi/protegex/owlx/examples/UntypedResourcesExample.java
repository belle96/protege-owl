package edu.stanford.smi.protegex.owlx.examples;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFUntypedResource;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class UntypedResourcesExample {

    public static void main(String[] args) throws OntologyLoadException {
        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModel();

        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
        OWLIndividual individual = personClass.createOWLIndividual("Darwin");
        RDFProperty hasImageProperty = owlModel.createRDFProperty("hasImage");

        String uri = "http://www.knublauch.com/darwin/Darwin-Feeding-Smiling.jpg";
        RDFUntypedResource image = owlModel.createRDFUntypedResource(uri);
        individual.addPropertyValue(hasImageProperty, image);

        Jena.dumpRDF(owlModel.getOntModel());
    }
}
