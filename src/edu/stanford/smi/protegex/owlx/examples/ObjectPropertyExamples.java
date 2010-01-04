package edu.stanford.smi.protegex.owlx.examples;

import java.util.Collection;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ObjectPropertyExamples {

    public static void main(String[] args) throws OntologyLoadException {

        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModel();

        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
        OWLNamedClass animalClass = owlModel.createOWLNamedClass("Animal");
        OWLObjectProperty childrenProperty = owlModel.createOWLObjectProperty("children");
        childrenProperty.addUnionRangeClass(personClass);
        childrenProperty.addUnionRangeClass(animalClass);

        childrenProperty.setDomain(personClass);
        childrenProperty.addUnionDomainClass(animalClass);
        // Now the domain of the property is Person or Animal

        // A subproperty inherits the domain of its superproperty
        OWLObjectProperty sonsProperty = owlModel.createOWLObjectProperty("sons");
        sonsProperty.addSuperproperty(childrenProperty);
        assert (sonsProperty.getDomain(false) == null);
        assert (sonsProperty.getDomain(true) instanceof OWLUnionClass);

        // Union domains can be resolved using getUnionDomain
        Collection unionDomain = sonsProperty.getUnionDomain(true);
        assert (unionDomain.contains(personClass));
        assert (unionDomain.contains(animalClass));

        OWLObjectProperty ancestorProperty = owlModel.createOWLObjectProperty("ancestor");
        ancestorProperty.setRange(personClass);
        ancestorProperty.setTransitive(true);

        Jena.dumpRDF(owlModel.getOntModel());
    }
}
