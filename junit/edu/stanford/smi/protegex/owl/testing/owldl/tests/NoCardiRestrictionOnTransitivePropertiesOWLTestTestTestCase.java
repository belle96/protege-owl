package edu.stanford.smi.protegex.owl.testing.owldl.tests;

import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.testing.owldl.NoCardiRestrictionOnTransitivePropertiesOWLDLTest;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class NoCardiRestrictionOnTransitivePropertiesOWLTestTestTestCase extends AbstractJenaTestCase {

    public void testReport() {
        OWLObjectProperty superSlot = owlModel.createOWLObjectProperty("super");
        OWLObjectProperty subSlot = owlModel.createOWLObjectProperty("sub");
        subSlot.setTransitive(true);
        subSlot.addSuperproperty(superSlot);
        RDFSClass restriction = owlModel.createOWLCardinality(subSlot, 1);
        assertTrue(NoCardiRestrictionOnTransitivePropertiesOWLDLTest.fails(restriction));
    }
}
