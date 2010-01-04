package edu.stanford.smi.protegex.owlx.examples;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class EnumeratedClassExample {

    public static void main(String[] args) throws OntologyLoadException {

        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModel();

        OWLNamedClass colorClass = owlModel.createOWLNamedClass("Color");
        OWLIndividual red = colorClass.createOWLIndividual("red");
        OWLIndividual yellow = colorClass.createOWLIndividual("yellow");
        OWLIndividual green = colorClass.createOWLIndividual("green");
        OWLIndividual black = colorClass.createOWLIndividual("black");

        OWLNamedClass trafficLightColor = owlModel.createOWLNamedClass("TrafficLightColor");
        OWLEnumeratedClass enumeratedClass = owlModel.createOWLEnumeratedClass();
        enumeratedClass.addOneOf(red);
        enumeratedClass.addOneOf(yellow);
        enumeratedClass.addOneOf(green);
        trafficLightColor.setDefinition(enumeratedClass);

        Jena.dumpRDF(owlModel.getOntModel());
    }
}
