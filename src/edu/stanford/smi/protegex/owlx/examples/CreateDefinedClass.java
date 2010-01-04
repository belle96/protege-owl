package edu.stanford.smi.protegex.owlx.examples;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

/**
 * Creates a "defined" class, i.e. a class with necessary and sufficient conditions.
 * Here, the class Parent is defined as a Person that has at least one child.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateDefinedClass {

    public static void main(String[] args) throws OntologyLoadException {

        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModel();

        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty hasChildrenProperty = owlModel.createOWLObjectProperty("hasChildren");
        hasChildrenProperty.setDomain(personClass);

        OWLNamedClass parentClass = owlModel.createOWLNamedClass("Parent");
        OWLMinCardinality minCardinality = owlModel.createOWLMinCardinality(hasChildrenProperty, 1);
        OWLIntersectionClass intersectionClass = owlModel.createOWLIntersectionClass();
        intersectionClass.addOperand(personClass);
        intersectionClass.addOperand(minCardinality);
        parentClass.setDefinition(intersectionClass);

        Jena.dumpRDF(owlModel.getOntModel());
    }
}
