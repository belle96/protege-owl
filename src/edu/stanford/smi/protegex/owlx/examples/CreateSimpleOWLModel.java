package edu.stanford.smi.protegex.owlx.examples;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;

/**
 * An example program that creates an empty OWLModel, fills it
 * with some example classes, properties and individuals, and
 * finally prints it in RDF syntax to the screen.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateSimpleOWLModel {

    public static void main(String[] args) throws OntologyLoadException {

        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModel();

        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");

        OWLDatatypeProperty ageProperty = owlModel.createOWLDatatypeProperty("age");
        ageProperty.setRange(owlModel.getXSDint());
        ageProperty.setDomain(personClass);

        OWLObjectProperty childrenProperty = owlModel.createOWLObjectProperty("children");
        childrenProperty.setRange(personClass);
        childrenProperty.setDomain(personClass);

        RDFIndividual darwin = personClass.createRDFIndividual("Darwin");
        darwin.setPropertyValue(ageProperty, new Integer(0));

        RDFIndividual holgi = personClass.createRDFIndividual("Holger");
        holgi.setPropertyValue(childrenProperty, darwin);
        holgi.setPropertyValue(ageProperty, new Integer(33));

        Jena.dumpRDF(owlModel.getOntModel());
    }
}
