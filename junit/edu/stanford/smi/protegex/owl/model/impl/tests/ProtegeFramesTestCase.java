package edu.stanford.smi.protegex.owl.model.impl.tests;

import java.util.Collection;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProtegeFramesTestCase extends AbstractJenaTestCase {

    public void testRelationFrames() {
        Cls cls = owlModel.getSystemFrames().getDirectedBinaryRelationCls();
        assertNotNull(cls);
        Slot fromSlot = owlModel.getSystemFrames().getFromSlot();
        assertNotNull(fromSlot);
        Slot toSlot = owlModel.getSystemFrames().getToSlot();
        assertNotNull(toSlot);
    }


    public void testPALFrames() {
        assertTrue(owlModel.getCls(ProtegeNames.Cls.PAL_CONSTRAINT) instanceof RDFSNamedClass);
        assertTrue(owlModel.getSlot(ProtegeNames.Slot.PAL_DESCRIPTION) instanceof RDFProperty);
        assertTrue(owlModel.getSlot(ProtegeNames.Slot.PAL_NAME) instanceof RDFProperty);
        assertTrue(owlModel.getSlot(ProtegeNames.Slot.PAL_RANGE) instanceof RDFProperty);
        assertTrue(owlModel.getSlot(ProtegeNames.Slot.PAL_STATEMENT) instanceof RDFProperty);
        assertTrue(owlModel.getSlot(ProtegeNames.Slot.CONSTRAINTS) instanceof RDFProperty);

        RDFSNamedClass constraintClass = owlModel.getRDFSNamedClass(ProtegeNames.Cls.PAL_CONSTRAINT);
        Collection properties = constraintClass.getUnionDomainProperties();
        assertSize(4, properties);
    }

}
