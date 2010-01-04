package edu.stanford.smi.protegex.owl.jena.importer.tests;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImportInstancesTestCase extends AbstractOWLImporterTestCase {

    public void testImportInstance() {
        OWLNamedClass oldCls = owlModel.createOWLNamedClass("Person");
        OWLDatatypeProperty oldAgeSlot = owlModel.createOWLDatatypeProperty("age", owlModel.getXSDint());
        OWLObjectProperty oldChildrenSlot = owlModel.createOWLObjectProperty("children");
        oldAgeSlot.setDomain(oldCls);
        oldChildrenSlot.setDomain(oldCls);
        Instance oldInstance = oldCls.createInstance("John");
        Instance oldChild = oldCls.createInstance("Child");
        oldInstance.setDirectOwnSlotValue(oldAgeSlot, new Integer(42));
        oldInstance.setDirectOwnSlotValue(oldChildrenSlot, oldChild);
        KnowledgeBase kb = runOWLImporter();
        Cls newCls = kb.getCls(oldCls.getName());
        Slot newAgeSlot = kb.getSlot(oldAgeSlot.getName());
        Slot newChildrenSlot = kb.getSlot(oldChildrenSlot.getName());
        Instance newInstance = kb.getInstance(oldInstance.getName());
        Instance newChild = kb.getInstance(oldChild.getName());
        assertNotNull(newInstance);
        assertEquals(newCls, newInstance.getDirectType());
        assertEquals(new Integer(42), newInstance.getDirectOwnSlotValue(newAgeSlot));
        assertEquals(newChild, newInstance.getDirectOwnSlotValue(newChildrenSlot));
    }
}
