package edu.stanford.smi.protegex.owl.testing.sanity.tests;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.testing.sanity.FacetConstraintsTest;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FacetConstraintsTestTestCase extends AbstractJenaTestCase {

    public void testProtegeCore() {

        OWLNamedClass maleCls = owlModel.createOWLNamedClass("Male");
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("isMale");
        slot.setFunctional(true);
        maleCls.addSuperclass(owlModel.createOWLHasValue(slot, Boolean.TRUE));
        RDFResource instance = (RDFResource) maleCls.createInstance("myMale");
        FacetConstraintsTest test = new FacetConstraintsTest();
        assertSize(0, test.test(instance));
    }
}
