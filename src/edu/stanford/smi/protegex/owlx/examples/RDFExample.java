package edu.stanford.smi.protegex.owlx.examples;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFExample {

    public static void main(String[] args) throws OntologyLoadException {

        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModel();

        RDFSNamedClass personClass = owlModel.createRDFSNamedClass("Person");
        RDFProperty ageProperty = owlModel.createRDFProperty("age");
        ageProperty.setRange(owlModel.getXSDint());
        ageProperty.setDomain(personClass);

        RDFIndividual individual = personClass.createRDFIndividual("Holger");
        individual.setPropertyValue(ageProperty, new Integer(33));

        Jena.dumpRDF(owlModel.getOntModel());
    }
}
