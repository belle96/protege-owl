package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLComplementClassTestCase extends AbstractJenaTestCase {

    public void testSimpleComplement() {

        RDFProperty complementOfProperty = owlModel.getRDFProperty(OWLNames.Slot.COMPLEMENT_OF);

        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLComplementClass complementClass = owlModel.createOWLComplementClass(cls);
        assertEquals(complementOfProperty, complementClass.getOperandsProperty());
        assertEquals(cls, complementClass.getComplement());
        assertSize(1, complementClass.getPropertyValues(complementOfProperty));
        assertEquals(cls, complementClass.getPropertyValue(complementOfProperty));

        OWLNamedClass newClass = owlModel.createOWLNamedClass("NewClass");
        complementClass.setPropertyValue(complementOfProperty, newClass);
        assertEquals(newClass, complementClass.getComplement());
    }
}
