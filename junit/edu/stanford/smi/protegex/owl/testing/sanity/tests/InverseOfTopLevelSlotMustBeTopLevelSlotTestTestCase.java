package edu.stanford.smi.protegex.owl.testing.sanity.tests;

import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.testing.sanity.InverseOfTopLevelPropertyMustBeTopLevelPropertyTest;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InverseOfTopLevelSlotMustBeTopLevelSlotTestTestCase extends AbstractJenaTestCase {

    public void testSimple() {
        OWLObjectProperty a = owlModel.createOWLObjectProperty("a");
        OWLObjectProperty x = owlModel.createOWLObjectProperty("x");
        a.setInverseProperty(x);
        assertFalse(InverseOfTopLevelPropertyMustBeTopLevelPropertyTest.fails(a));
        OWLObjectProperty y = owlModel.createOWLObjectProperty("y");
        x.addSuperproperty(y);
        assertTrue(InverseOfTopLevelPropertyMustBeTopLevelPropertyTest.fails(a));
        InverseOfTopLevelPropertyMustBeTopLevelPropertyTest.repair(a);
        assertFalse(InverseOfTopLevelPropertyMustBeTopLevelPropertyTest.fails(a));
    }
}
