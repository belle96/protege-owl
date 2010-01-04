package edu.stanford.smi.protegex.owl.testing.sanity.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.testing.sanity.PropertyRangeRedundancyTest;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyRangeRedundancyTestTestCase extends AbstractJenaTestCase {

    public void testSimpleCase() {
        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperCls");
        OWLNamedClass subCls = owlModel.createOWLNamedSubclass("SubCls", superCls);
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        slot.addUnionRangeClass(superCls);
        assertSize(0, PropertyRangeRedundancyTest.fails(slot));
        slot.addUnionRangeClass(subCls);
        assertSize(1, PropertyRangeRedundancyTest.fails(slot));
        assertContains(subCls, PropertyRangeRedundancyTest.fails(slot));
    }
}
