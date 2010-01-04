package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadOWLDataRangeTestCase extends AbstractJenaTestCase {

    public void testLoadFloatRestriction() throws Exception {
        OWLNamedClass oldClass = owlModel.createOWLNamedClass("Class");
        OWLDatatypeProperty oldProperty = owlModel.createOWLDatatypeProperty("property");
        RDFSLiteral oldFirstLiteral = owlModel.createRDFSLiteral("1.1", owlModel.getXSDfloat());
        RDFSLiteral oldSecondLiteral = owlModel.createRDFSLiteral("2.2", owlModel.getXSDfloat());
        OWLDataRange oldDataRange = owlModel.createOWLDataRange(new RDFSLiteral[]{
                oldFirstLiteral,
                oldSecondLiteral
        });
        OWLAllValuesFrom oldAllValuesFrom = owlModel.createOWLAllValuesFrom(oldProperty, oldDataRange);
        oldClass.addSuperclass(oldAllValuesFrom);

        JenaOWLModel newModel = reload(owlModel);
        OWLNamedClass newClass = newModel.getOWLNamedClass(oldClass.getName());
        OWLDatatypeProperty newProperty = newModel.getOWLDatatypeProperty(oldProperty.getName());
        OWLAllValuesFrom newAllValuesFrom = (OWLAllValuesFrom) newClass.getRestrictions().iterator().next();
        assertEquals(newProperty, newAllValuesFrom.getOnProperty());
        OWLDataRange newDataRange = (OWLDataRange) newAllValuesFrom.getAllValuesFrom();
        assertSize(2, newDataRange.getOneOfValues());
        assertEquals(new Float(1.1), newDataRange.getOneOfValues().get(0));
        assertEquals(new Float(2.2), newDataRange.getOneOfValues().get(1));
    }


    public void testLoadFloatRestrictionFromFile() throws Exception {
        loadRemoteOntology("datatypeEnumerationFloat.owl");
        OWLNamedClass cls = owlModel.getOWLNamedClass("Cls");
        OWLAllValuesFrom restriction = (OWLAllValuesFrom) cls.getRestrictions(false).iterator().next();
        RDFResource filler = restriction.getFiller();
        assertTrue(filler instanceof OWLDataRange);
        final Collection values = ((OWLDataRange) filler).getOneOf().getValues();
        assertSize(2, values);
        assertContains(new Float(1.1), values);
        assertContains(new Float(2.2), values);
    }


    public void testLoadIntRestrictionFromFile() throws Exception {
        loadRemoteOntology("datatypeEnumerationInt.owl");
        OWLNamedClass cls = owlModel.getOWLNamedClass("Cls");
        OWLAllValuesFrom restriction = (OWLAllValuesFrom) cls.getRestrictions(false).iterator().next();
        RDFResource filler = restriction.getFiller();
        assertTrue(filler instanceof OWLDataRange);
        Collection values = ((OWLDataRange) filler).getOneOf().getValues();
        assertSize(2, values);
        assertContains(new Integer(1), values);
        assertContains(new Integer(2), values);
    }


    public void testLoadStringRestrictionFromFile() throws Exception {
        loadRemoteOntology("datatypeEnumerationString.owl");
        OWLNamedClass cls = owlModel.getOWLNamedClass("Cls");
        OWLAllValuesFrom restriction = (OWLAllValuesFrom) cls.getRestrictions(false).iterator().next();
        RDFResource filler = restriction.getFiller();
        assertTrue(filler instanceof OWLDataRange);
        Collection values = ((OWLDataRange) filler).getOneOf().getValues();
        assertSize(2, values);
        assertContains("A", values);
        assertContains("B", values);
    }


    public void testLoadOWLDataRangeValue() throws Exception {
        loadRemoteOntology("owlDataRangeValue.owl");
        RDFProperty property = owlModel.getRDFProperty("property");
        assertNotNull(property);
        assertTrue(property.getRange() instanceof OWLDataRange);
        RDFResource individual = owlModel.getRDFResource("Test");
        assertNotNull(individual);
        assertSize(1, individual.getPropertyValues(property));
    }
}
