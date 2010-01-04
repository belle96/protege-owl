package edu.stanford.smi.protegex.owl.jena.importer.tests;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;

import java.util.Collections;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImportSlotsTestCase extends AbstractOWLImporterTestCase {

    public void testImportIntegerSlot() {
        OWLNamedClass dummyCls = owlModel.createOWLNamedClass("Dummy");
        OWLDatatypeProperty oldSlot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDint());
        oldSlot.setDomain(dummyCls);
        oldSlot.setFunctional(true);
        KnowledgeBase kb = runOWLImporter();
        Slot slot = kb.getSlot(oldSlot.getName());
        assertNotNull(slot);
        assertEquals(ValueType.INTEGER, slot.getValueType());
        assertFalse(slot.getAllowsMultipleValues());
        assertSize(1, slot.getDirectDomain());
    }


    public void testImportSymbolSlot() {
        OWLNamedClass dummyCls = owlModel.createOWLNamedClass("Dummy");
        OWLDatatypeProperty oldSlot = owlModel.createOWLDatatypeProperty("slot", new RDFSLiteral[]{
                owlModel.createRDFSLiteral("value")
        });
        oldSlot.setDomain(dummyCls);
        KnowledgeBase kb = runOWLImporter();
        Slot slot = kb.getSlot(oldSlot.getName());
        assertNotNull(slot);
        assertEquals(ValueType.SYMBOL, slot.getValueType());
        assertSize(1, slot.getAllowedValues());
        assertEquals("value", slot.getAllowedValues().iterator().next());
    }


    public void testImportObjectSlot() {
        OWLNamedClass dummyCls = owlModel.createOWLNamedClass("Dummy");
        OWLObjectProperty oldSlot = owlModel.createOWLObjectProperty("slot");
        OWLObjectProperty oldInverseSlot = owlModel.createOWLObjectProperty("inverse");
        oldSlot.setInverseProperty(oldInverseSlot);
        oldSlot.setDomain(dummyCls);
        oldSlot.setFunctional(false);
        oldSlot.setUnionRangeClasses(Collections.singleton(dummyCls));
        KnowledgeBase kb = runOWLImporter();
        Slot slot = kb.getSlot(oldSlot.getName());
        assertNotNull(slot);
        assertTrue(oldSlot.hasObjectRange());
        assertTrue(slot.getAllowsMultipleValues());
        assertSize(1, slot.getAllowedClses());
        assertEquals(kb.getCls(dummyCls.getName()), slot.getAllowedClses().iterator().next());
        Slot newInverseSlot = kb.getSlot(oldInverseSlot.getName());
        assertNotNull(newInverseSlot);
        assertEquals(newInverseSlot, slot.getInverseSlot());
    }


    public void testImportDomainlessSlotWithRestriction() {
        OWLNamedClass oldCls = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty oldSlot = owlModel.createOWLObjectProperty("oldSlot");
        oldSlot.setDomainDefined(false);
        oldCls.addSuperclass(owlModel.createOWLAllValuesFrom(oldSlot, oldCls));
        KnowledgeBase kb = runOWLImporter();
        Slot newSlot = kb.getSlot(oldSlot.getName());
        assertSize(1, newSlot.getDirectDomain());
        Cls newCls = kb.getCls(oldCls.getName());
        assertContains(newCls, newSlot.getDirectDomain());
        assertContains(newCls, newCls.getTemplateSlotAllowedClses(newSlot));
    }


    public void testImportDomainlessSlotWithoutRestriction() {
        OWLObjectProperty oldSlot = owlModel.createOWLObjectProperty("oldSlot");
        oldSlot.setDomainDefined(false);
        KnowledgeBase kb = runOWLImporter();
        Slot newSlot = kb.getSlot(oldSlot.getName());
        assertSize(1, newSlot.getDirectDomain());
        assertContains(kb.getRootCls(), newSlot.getDirectDomain());
        assertSize(1, kb.getRootCls().getDirectTemplateSlots());
    }
}
