package edu.stanford.smi.protegex.owlx.examples;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

/**
 * Creates a class Parent as a subclass of Person, where each Parent has the necessary
 * condition that he or she has at least one child.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateRestriction {

    public static void main(String[] args) throws OntologyLoadException {

        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModel();

        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty hasChildrenProperty = owlModel.createOWLObjectProperty("hasChildren");
        hasChildrenProperty.setDomain(personClass);

        OWLNamedClass parentClass = owlModel.createOWLNamedSubclass("Parent", personClass);
        OWLMinCardinality minCardinality = owlModel.createOWLMinCardinality(hasChildrenProperty, 1);
        parentClass.addSuperclass(minCardinality);

        assert (parentClass.getRestrictions(true).contains(minCardinality));

        Jena.dumpRDF(owlModel.getOntModel());
    }
}
