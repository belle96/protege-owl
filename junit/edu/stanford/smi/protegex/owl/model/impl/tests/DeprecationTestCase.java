package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeprecationTestCase extends AbstractJenaTestCase {

    public void testDeprecatedClass() {
        RDFSNamedClass namedClass = owlModel.createRDFSNamedClass("cls");
        assertFalse(namedClass.isDeprecated());
        namedClass.setDeprecated(true);
        assertTrue(namedClass.isDeprecated());
        assertTrue(namedClass.getProtegeTypes().contains(owlModel.getRDFSNamedClass(OWLNames.Cls.DEPRECATED_CLASS)));
        assertSize(2, namedClass.getProtegeTypes());
        namedClass.setDeprecated(false);
        assertFalse(namedClass.isDeprecated());
    }


    public void testDeprecatedProperty() {
        RDFProperty property = owlModel.createRDFProperty("property");
        assertFalse(property.isDeprecated());
        property.setDeprecated(true);
        assertTrue(property.isDeprecated());
        assertTrue(property.getProtegeTypes().contains(owlModel.getRDFSNamedClass(OWLNames.Cls.DEPRECATED_PROPERTY)));
        assertSize(2, property.getProtegeTypes());
        property.setDeprecated(false);
        assertFalse(property.isDeprecated());
    }
}
