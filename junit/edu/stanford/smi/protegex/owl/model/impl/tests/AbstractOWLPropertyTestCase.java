package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AbstractOWLPropertyTestCase extends AbstractJenaTestCase {

    public void testInverseFunctionalProperty() {
        RDFSClass metaclass = owlModel.getRDFSNamedClass(OWLNames.Cls.INVERSE_FUNCTIONAL_PROPERTY);
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        assertFalse(property.isInverseFunctional());
        property.setInverseFunctional(true);
        assertTrue(property.isInverseFunctional());
        assertSize(2, property.getProtegeTypes());
        assertTrue(property.hasProtegeType(metaclass));
        property.setInverseFunctional(false);
        assertFalse(property.isInverseFunctional());
        assertSize(1, property.getProtegeTypes());
        property.addProtegeType(metaclass);
        assertTrue(property.isInverseFunctional());
    }


    public void testInverseFunctionalSuperproperty() {
        RDFSNamedClass metaclass = owlModel.getRDFSNamedClass(OWLNames.Cls.INVERSE_FUNCTIONAL_PROPERTY);
        OWLProperty superproperty = owlModel.createOWLDatatypeProperty("super");
        OWLProperty subproperty = owlModel.createOWLDatatypeProperty("sub");
        subproperty.addSuperproperty(superproperty);
        superproperty.setInverseFunctional(true);
        assertFalse(subproperty.hasProtegeType(metaclass));
        assertTrue(subproperty.isInverseFunctional());
    }
}
