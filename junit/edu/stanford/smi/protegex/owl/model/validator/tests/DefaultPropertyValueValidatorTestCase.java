package edu.stanford.smi.protegex.owl.model.validator.tests;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSDatatypeFactory;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultPropertyValueValidatorTestCase extends AbstractJenaTestCase {


    private RDFSDatatypeFactory factory;


    public void testIntegerRangeExclusive() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        factory = owlModel.getRDFSDatatypeFactory();
        RDFResource subject = owlModel.getOWLThingClass();
        RDFProperty property = owlModel.createRDFProperty("property");
        RDFSDatatype datatype = factory.createAnonymousDatatype(owlModel.getXSDint());
        factory.setMinExclusive(datatype, owlModel.createRDFSLiteral(new Integer(2)));
        factory.setMaxExclusive(datatype, owlModel.createRDFSLiteral(new Integer(4)));
        property.setRange(datatype);
        assertFalse(subject.isValidPropertyValue(property, new Integer(1)));
        assertFalse(subject.isValidPropertyValue(property, new Integer(2)));
        assertTrue(subject.isValidPropertyValue(property, new Integer(3)));
        assertFalse(subject.isValidPropertyValue(property, new Integer(4)));
        assertFalse(subject.isValidPropertyValue(property, new Integer(5)));
    }


    public void testIntegerRangeInclusive() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        factory = owlModel.getRDFSDatatypeFactory();
        RDFResource subject = owlThing;
        RDFProperty property = owlModel.createRDFProperty("property");
        RDFSDatatype datatype = factory.createAnonymousDatatype(owlModel.getXSDint());
        factory.setMinInclusive(datatype, owlModel.createRDFSLiteral(new Integer(2)));
        factory.setMaxInclusive(datatype, owlModel.createRDFSLiteral(new Integer(4)));
        property.setRange(datatype);
        assertFalse(subject.isValidPropertyValue(property, new Integer(1)));
        assertTrue(subject.isValidPropertyValue(property, new Integer(2)));
        assertTrue(subject.isValidPropertyValue(property, new Integer(4)));
        assertFalse(subject.isValidPropertyValue(property, new Integer(5)));
    }


    public void testStringLength() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        factory = owlModel.getRDFSDatatypeFactory();
        RDFResource subject = owlThing;
        RDFProperty property = owlModel.createRDFProperty("property");
        RDFSDatatype datatype = factory.createAnonymousDatatype(owlModel.getXSDstring());
        factory.setLength(datatype, 4);
        property.setRange(datatype);
        assertTrue(subject.isValidPropertyValue(property, "aldi"));
        assertFalse(subject.isValidPropertyValue(property, "holgi"));
    }


    public void testStringMinMaxLength() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        factory = owlModel.getRDFSDatatypeFactory();
        RDFResource subject = owlThing;
        RDFProperty property = owlModel.createRDFProperty("property");
        RDFSDatatype datatype = factory.createAnonymousDatatype(owlModel.getXSDstring());
        factory.setMinLength(datatype, 2);
        factory.setMaxLength(datatype, 4);
        property.setRange(datatype);
        assertFalse(subject.isValidPropertyValue(property, "h"));
        assertTrue(subject.isValidPropertyValue(property, "aldi"));
        assertFalse(subject.isValidPropertyValue(property, "holgi"));
    }
}
