package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreatePropertiesTestCase extends AbstractJenaCreatorTestCase {


    public void testCreateSimpleRDFProperty() {
        RDFProperty slot = owlModel.createRDFProperty("slot");
        String uri = slot.getURI();
        OntModel newModel = runJenaCreator();
        OntProperty property = newModel.getOntProperty(uri);
        assertNotNull(property);
        assertEquals(RDF.Property, property.getRDFType());
    }


    public void testCreateSimpleDatatypeProperty() {
        OWLProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDstring());
        String uri = slot.getURI();
        OntModel newModel = runJenaCreator();
        DatatypeProperty property = newModel.getDatatypeProperty(uri);
        assertNotNull(property);
        assertEquals(OWL.DatatypeProperty, property.getRDFType());
    }


    public void testCreateSimpleObjectProperty() {
        OWLProperty slot = owlModel.createOWLObjectProperty("slot");
        String uri = slot.getURI();
        OntModel newModel = runJenaCreator();
        ObjectProperty property = newModel.getObjectProperty(uri);
        assertNotNull(property);
        assertEquals(OWL.ObjectProperty, property.getRDFType());
    }


    public void testDatatypePropertyWithNoRange() {
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", (RDFSDatatype) null);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        slot.addUnionDomainClass(cls);
        RDFResource instance = cls.createInstance("instance");
        instance.addPropertyValue(slot, Boolean.TRUE);
        instance.addPropertyValue(slot, new Float(4.2));
        instance.addPropertyValue(slot, new Integer(42));
        instance.addPropertyValue(slot, "Holgi");
        assertSize(4, instance.getPropertyValues(slot, true));
        OntModel newModel = runJenaCreator();
        DatatypeProperty property = newModel.getDatatypeProperty(slot.getURI());
        assertNull(property.getRange());
        Individual individual = newModel.getIndividual(instance.getURI());
        assertSize(4, individual.listPropertyValues(property));
        assertHasValue(individual, property, ValueType.BOOLEAN, Boolean.TRUE);
        assertHasValue(individual, property, ValueType.FLOAT, new Float(4.2));
        assertHasValue(individual, property, ValueType.INTEGER, new Integer(42));
        assertHasValue(individual, property, ValueType.STRING, "Holgi");
    }


    public void testEquivalentProperties() {
        OWLProperty equivalentSlot = owlModel.createOWLObjectProperty("equivalent");
        OWLProperty slot = owlModel.createOWLObjectProperty("slot");
        slot.addEquivalentProperty(equivalentSlot);
        OntModel newModel = runJenaCreator();
        ObjectProperty equivalentProperty = newModel.getObjectProperty(equivalentSlot.getURI());
        ObjectProperty property = newModel.getObjectProperty(slot.getURI());
        assertEquals(equivalentProperty, property.getEquivalentProperty());
    }


    public void testFunctionalProperty() {
        OWLProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDstring());
        slot.setFunctional(true);
        OWLProperty other = owlModel.createOWLDatatypeProperty("other", owlModel.getXSDstring());
        other.setFunctional(false);
        OntModel newModel = runJenaCreator();
        DatatypeProperty property = newModel.getDatatypeProperty(slot.getURI());
        assertTrue(property.hasRDFType(OWL.FunctionalProperty));
        DatatypeProperty otherProperty = newModel.getDatatypeProperty(other.getURI());
        assertFalse(otherProperty.hasRDFType(OWL.FunctionalProperty));
    }


    public void testInverseProperty() {
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        OWLObjectProperty inverseSlot = owlModel.createOWLObjectProperty("inverse");
        slot.setInverseProperty(inverseSlot);
        OntModel newModel = runJenaCreator();
        ObjectProperty property = newModel.getObjectProperty(slot.getURI());
        ObjectProperty inverseProperty = newModel.getObjectProperty(inverseSlot.getURI());
        assertNotNull(property);
        assertNotNull(inverseProperty);
        assertTrue(property.hasInverse());
        assertEquals(inverseProperty, property.getInverse());
    }


    public void testInverseFunctionalProperty() {
        OWLProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDstring());
        slot.setInverseFunctional(true);
        OWLProperty other = owlModel.createOWLDatatypeProperty("other", owlModel.getXSDstring());
        other.setInverseFunctional(false);
        OntModel newModel = runJenaCreator();
        DatatypeProperty property = newModel.getDatatypeProperty(slot.getURI());
        assertTrue(property.hasRDFType(OWL.InverseFunctionalProperty));
        DatatypeProperty otherProperty = newModel.getDatatypeProperty(other.getURI());
        assertFalse(otherProperty.hasRDFType(OWL.InverseFunctionalProperty));
    }


    public void testSuperProperties() {
        OWLProperty superSlot = owlModel.createOWLObjectProperty("super");
        OWLProperty subSlot = owlModel.createOWLObjectProperty("sub");
        subSlot.addSuperproperty(superSlot);
        OntModel newModel = runJenaCreator();
        ObjectProperty superProperty = newModel.getObjectProperty(superSlot.getURI());
        ObjectProperty subProperty = newModel.getObjectProperty(subSlot.getURI());
        assertEquals(subProperty, superProperty.getSubProperty());
        assertEquals(superProperty, subProperty.getSuperProperty());
    }


    public void testSymmetricProperty() {
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        slot.setSymmetric(true);
        OWLObjectProperty other = owlModel.createOWLObjectProperty("other");
        other.setSymmetric(false);
        OntModel newModel = runJenaCreator();
        ObjectProperty property = newModel.getObjectProperty(slot.getURI());
        assertTrue(property.hasRDFType(OWL.SymmetricProperty));
        ObjectProperty otherProperty = newModel.getObjectProperty(other.getURI());
        assertFalse(otherProperty.hasRDFType(OWL.SymmetricProperty));
    }


    public void testTransitiveProperty() {
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        slot.setTransitive(true);
        OWLObjectProperty other = owlModel.createOWLObjectProperty("other");
        other.setTransitive(false);
        OntModel newModel = runJenaCreator();
        ObjectProperty property = newModel.getObjectProperty(slot.getURI());
        assertTrue(property.hasRDFType(OWL.TransitiveProperty));
        ObjectProperty otherProperty = newModel.getObjectProperty(other.getURI());
        assertFalse(otherProperty.hasRDFType(OWL.TransitiveProperty));
    }


    public void testDomainlessPropertyValues() {
        OWLNamedClass wordClass = owlModel.createOWLNamedClass("Word");
        OWLDatatypeProperty nameprop = owlModel.createOWLDatatypeProperty("name");
        wordClass.createOWLIndividual("thing").setPropertyValue(nameprop, "something");
        wordClass.setPropertyValue(nameprop, "some");

        OntModel ontModel = runJenaCreator();
        OntClass wordOntClass = ontModel.getOntClass(wordClass.getURI());
        OntProperty property = ontModel.getOntProperty(nameprop.getURI());
        assertNotNull(property);
        assertSize(1, wordOntClass.listPropertyValues(property));
    }
}
