package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Set;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class GetAssociatedPropertiesTestCase extends AbstractJenaTestCase {

    public void testRDFSNamedClassSimpleProperty() {
        owlModel.createRDFProperty("domainless");
        RDFProperty domainful = owlModel.createRDFProperty("domainful");
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        domainful.setDomain(cls);
        Set associatedProperties = cls.getAssociatedProperties();
        assertSize(1, associatedProperties);
        assertContains(domainful, cls.getAssociatedProperties());
    }


    public void testRDFSNamedClassSubclass() {
        owlModel.createRDFProperty("domainless");
        RDFProperty domainful = owlModel.createRDFProperty("domainful");
        RDFSNamedClass superclass = owlModel.createRDFSNamedClass("Superclass");
        RDFSNamedClass cls = owlModel.createRDFSNamedSubclass("Class", superclass);
        domainful.setDomain(superclass);
        Set associatedProperties = cls.getAssociatedProperties();
        assertSize(1, associatedProperties);
        assertContains(domainful, cls.getAssociatedProperties());
    }


    public void testRDFSNamedClassSubproperty() {
        owlModel.createRDFProperty("domainless");
        RDFProperty domainful = owlModel.createRDFProperty("domainful");
        RDFProperty subproperty = owlModel.createRDFProperty("subproperty");
        subproperty.addSuperproperty(domainful);
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        domainful.setDomain(cls);
        Set associatedProperties = cls.getAssociatedProperties();
        assertSize(2, associatedProperties);
        assertContains(domainful, cls.getAssociatedProperties());
        assertContains(subproperty, cls.getAssociatedProperties());
    }


    public void testOWLNamedClassRestriction() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        assertSize(0, cls.getAssociatedProperties());
        cls.addSuperclass(owlModel.createOWLMinCardinality(property, 1));
        assertSize(1, cls.getAssociatedProperties());
        assertContains(property, cls.getAssociatedProperties());
    }


    public void testOWLNamedClassWithDomainButMaxCardinality() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        assertSize(0, cls.getAssociatedProperties());
        property.setDomain(cls);
        assertSize(1, cls.getAssociatedProperties());
        cls.addSuperclass(owlModel.createOWLMaxCardinality(property, 0));
        assertSize(0, cls.getAssociatedProperties());
    }
}
