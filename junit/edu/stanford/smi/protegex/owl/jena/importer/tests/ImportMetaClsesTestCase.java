package edu.stanford.smi.protegex.owl.jena.importer.tests;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImportMetaClsesTestCase extends AbstractOWLImporterTestCase {

    public void testImportMetaclass() {
        OWLNamedClass oldMetaCls = (OWLNamedClass) owlModel.createOWLNamedSubclass("MetaCls", owlModel.getOWLNamedClassClass());
        OWLDatatypeProperty oldSlot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDint());
        oldSlot.setDomain(oldMetaCls);
        OWLNamedClass oldCls = (OWLNamedClass) oldMetaCls.createInstance("Cls");
        oldCls.setPropertyValue(oldSlot, new Integer(42));
        KnowledgeBase kb = runOWLImporter();
        Cls newMetaCls = kb.getCls(oldMetaCls.getName());
        Slot newSlot = kb.getSlot(oldSlot.getName());
        assertNotNull(newMetaCls);
        assertEquals(1, newMetaCls.getDirectSuperclassCount());
        assertEquals(kb.getDefaultClsMetaCls(), newMetaCls.getDirectSuperclasses().iterator().next());
        Cls newCls = kb.getCls(oldCls.getName());
        assertNotNull(newCls);
        assertEquals(newMetaCls, newCls.getDirectType());
        assertEquals(new Integer(42), newCls.getDirectOwnSlotValue(newSlot));
    }
}
