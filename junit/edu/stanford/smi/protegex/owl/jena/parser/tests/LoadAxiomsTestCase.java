package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadAxiomsTestCase extends AbstractJenaTestCase {

    public void testLoadSeanAxiomAnonymous() throws Exception {
        loadRemoteOntology("seanAxiomAnonymous.owl");
        Collection classes = new ArrayList(owlModel.getUserDefinedOWLNamedClasses());
        assertSize(3, classes);
        classes.remove(owlModel.getOWLNamedClass("Plant"));
        classes.remove(owlModel.getOWLNamedClass("Animal"));
        assertSize(1, classes);
        OWLNamedClass axiomClass = (OWLNamedClass) classes.iterator().next();
        assertNotNull(axiomClass);
        assertSize(1, axiomClass.getEquivalentClasses());
        RDFSClass definition = axiomClass.getDefinition();
        assertTrue(definition instanceof OWLUnionClass);
        assertSize(1, axiomClass.getDisjointClasses());
    }


    public void testLoadSeanAxiomNamed() throws Exception {
        loadRemoteOntology("seanAxiomNamed.owl");
        assertSize(3, owlModel.getUserDefinedOWLNamedClasses());
        OWLNamedClass axiomClass = owlModel.getOWLNamedClass("PlantOrPartOfPlant");
        assertNotNull(axiomClass);
        assertSize(1, axiomClass.getEquivalentClasses());
        RDFSClass definition = axiomClass.getDefinition();
        assertTrue(definition instanceof OWLUnionClass);
        assertSize(1, axiomClass.getDisjointClasses());
    }
}
