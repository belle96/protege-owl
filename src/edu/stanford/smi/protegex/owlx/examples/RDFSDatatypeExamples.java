package edu.stanford.smi.protegex.owlx.examples;

import junit.framework.Assert;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;

/**
 * Illustrates the use of XML Schema datatypes, values and RDFS Literals.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFSDatatypeExamples {

    public static void main(String[] args) throws OntologyLoadException {

        OWLModel owlModel = ProtegeOWL.createJenaOWLModel();
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLIndividual individual = cls.createOWLIndividual("Individual");

        // The four default datatypes are rendered into primitive Java types
        OWLDatatypeProperty stringProperty = owlModel.createOWLDatatypeProperty("stringProperty", owlModel.getXSDstring());
        individual.setPropertyValue(stringProperty, "MyString");
        String stringValue = (String) individual.getPropertyValue(stringProperty);

        OWLDatatypeProperty booleanProperty = owlModel.createOWLDatatypeProperty("booleanProperty", owlModel.getXSDboolean());
        individual.setPropertyValue(booleanProperty, Boolean.TRUE);
        Boolean booleanValue = (Boolean) individual.getPropertyValue(booleanProperty);

        OWLDatatypeProperty floatProperty = owlModel.createOWLDatatypeProperty("floatProperty", owlModel.getXSDfloat());
        individual.setPropertyValue(floatProperty, new Float(4.2));
        Float floatValue = (Float) individual.getPropertyValue(floatProperty);

        OWLDatatypeProperty intProperty = owlModel.createOWLDatatypeProperty("intProperty", owlModel.getXSDint());
        individual.setPropertyValue(floatProperty, new Integer(42));
        Integer intValue = (Integer) individual.getPropertyValue(intProperty);

        // If you prefer to get the value as RDFSLiteral instead of primitive objects
        RDFSLiteral intLiteral = individual.getPropertyValueLiteral(intProperty);
        Assert.assertEquals(intLiteral.getInt(), intValue.intValue());
        Assert.assertTrue(intLiteral.getDatatype().equals(owlModel.getXSDint()));

        // Values of non-default datatypes must be wrapped into RDFSLiterals
        RDFSDatatype xsdDate = owlModel.getRDFSDatatypeByName("xsd:date");
        OWLDatatypeProperty dateProperty = owlModel.createOWLDatatypeProperty("dateProperty", xsdDate);
        RDFSLiteral dateLiteral = owlModel.createRDFSLiteral("1971-07-06", xsdDate);
        individual.setPropertyValue(dateProperty, dateLiteral);
        RDFSLiteral myDate = (RDFSLiteral) individual.getPropertyValue(dateProperty);
        System.out.println("Date: " + myDate);

        // Strings with language tags must be wrapped into RDFSLiterals
        RDFSLiteral langLiteral = owlModel.createRDFSLiteral("Wert", "de");
        individual.setPropertyValue(stringProperty, langLiteral);
        RDFSLiteral result = (RDFSLiteral) individual.getPropertyValue(stringProperty);
        Assert.assertTrue(result.getLanguage().equals("de"));
        Assert.assertTrue(result.getString().equals("Wert"));
    }
}
