package edu.stanford.smi.protegex.owl.model.triplestore.impl.tests;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateRDFSRangeTestCase extends AbstractTripleStoreTestCase {


    public void testStringRange() {
        RDFProperty property = owlModel.createRDFProperty("property");
        ts.add(property, owlModel.getRDFSRangeProperty(), owlModel.getXSDstring());
        assertEquals(ValueType.STRING, ((Slot) property).getValueType());
        assertSize(0, ((Slot) property).getAllowedValues());
    }


    public void testObjectRange() {
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        RDFProperty property = owlModel.createRDFProperty("property");
        ts.add(property, owlModel.getRDFSRangeProperty(), cls);
        assertEquals(ValueType.INSTANCE, ((Slot) property).getValueType());
        assertSize(1, ((Slot) property).getAllowedClses());
        assertContains(cls, ((Slot) property).getAllowedClses());
    }
}
