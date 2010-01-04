package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.net.URI;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeletePropertyTestCase extends AbstractJenaTestCase {

    public void testDeletePropertyValues() {
        RDFProperty property = owlModel.createRDFProperty("property");
        RDFResource subject = owlThing;
        subject.addPropertyValue(property, "Test");
        assertSize(1, subject.getPropertyValues(property));
        property.delete();
        Iterator triples = owlModel.getTripleStoreModel().getActiveTripleStore().listTriples();
        triples.next();   // Skip over owl:Ontology triple
        assertFalse(triples.hasNext());
    }


    public void testDeleteEquivalentClassOnPropertyDeleteProgrammatically() {
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass superclass = owlModel.createOWLNamedClass("Superclass");
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        OWLRestriction restriction = owlModel.createOWLMinCardinality(property, 1);
        OWLIntersectionClass intersectionClass = owlModel.createOWLIntersectionClass();
        intersectionClass.addOperand(superclass);
        intersectionClass.addOperand(restriction);
        c.setDefinition(intersectionClass);
        c.removeSuperclass(owlThing);
        assertSize(1, c.getNamedSuperclasses(false));
        assertContains(superclass, c.getNamedSuperclasses(false));
        assertSize(1, c.getPropertyValues(owlModel.getRDFSSubClassOfProperty()));
        assertContains(superclass, c.getPropertyValues(owlModel.getRDFSSubClassOfProperty()));
        property.delete();
        assertSize(1, c.getNamedSuperclasses(false));
        assertContains(superclass, c.getNamedSuperclasses(false));
        assertSize(1, c.getPropertyValues(owlModel.getRDFSSubClassOfProperty()));
        assertContains(superclass, c.getPropertyValues(owlModel.getRDFSSubClassOfProperty()));
    }


    public void testDeleteEquivalentClassOnPropertyDeleteFromFile() throws Exception {
        loadTestOntology(new URI(getRemoteOntologyRoot() + "travel.owl"));
        OWLNamedClass cls = owlModel.getOWLNamedClass("BudgetHotelDestination");
        assertTrue(cls.isDefinedClass());
        RDFProperty s = owlModel.getRDFSSubClassOfProperty();
        assertSize(0, cls.getPropertyValues(s));
        RDFProperty property = owlModel.getRDFProperty("hasAccommodation");
        property.delete();
        assertSize(1, cls.getPropertyValues(s));
        OWLNamedClass superclass = (OWLNamedClass) cls.getPropertyValues(s).iterator().next();
        assertEquals("http://www.owl-ontologies.com/travel.owl#Destination", superclass.getName());
    }
}
