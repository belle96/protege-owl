package edu.stanford.smi.protegex.owlx.examples;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JenaOntModelExample {

    public static void main(String[] args) throws OntologyLoadException {

        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModel();

        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        OWLIndividual individual = cls.createOWLIndividual("Individual");

        OntModel ontModel = owlModel.getOntModel();
        OntClass ontClass = ontModel.getOntClass(cls.getURI());
        DatatypeProperty ontProperty = ontModel.getDatatypeProperty(property.getURI());
        Individual ontIndividual = ontModel.getIndividual(individual.getURI());

        // Do anything what you like with the OntModel resources...
    }
}
