package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadObjectAnnotationPropertyValuesTestCase extends AbstractJenaTestCase {

    public void testLoadSeeAlso() throws Exception {
        loadRemoteOntology("seeAlso.owl");
        OWLNamedClass cls = owlModel.getOWLNamedClass("Cls");
        OWLNamedClass otherCls = owlModel.getOWLNamedClass("OtherCls");
        Instance instance = owlModel.getRDFIndividual("Instance");
        RDFProperty seeAlsoSlot = (RDFProperty) owlModel.getSlot(RDFSNames.Slot.SEE_ALSO);
        Object value = cls.getPropertyValue(seeAlsoSlot);
        assertTrue(value instanceof Instance);
        assertEquals(instance, value);
        assertEquals(instance, otherCls.getPropertyValue(seeAlsoSlot));
    }
}
