package edu.stanford.smi.protegex.owl.jena.importer.tests;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImportFacetOverloadsTestCase extends AbstractOWLImporterTestCase {


    public void testImportAllRestrictionAtDatatypeSlot() {
        OWLNamedClass oldCls = owlModel.createOWLNamedClass("Cls");
        OWLDatatypeProperty oldSlot = owlModel.createOWLDatatypeProperty("hasValue");
        oldSlot.setDomain(oldCls);
        oldCls.addSuperclass(owlModel.createOWLAllValuesFrom(oldSlot, owlModel.getXSDint()));
        KnowledgeBase kb = runOWLImporter();
        Cls newCls = kb.getCls(oldCls.getName());
        Slot newSlot = kb.getSlot(oldSlot.getName());
        assertEquals(ValueType.ANY, newSlot.getValueType());
        assertEquals(ValueType.INTEGER, newCls.getTemplateSlotValueType(newSlot));
    }


    public void testImportAllRestrictionAtObjectSlot() {
        OWLNamedClass oldCls = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty oldSlot = owlModel.createOWLObjectProperty("hasValue");
        oldSlot.setDomain(oldCls);
        oldCls.addSuperclass(owlModel.createOWLAllValuesFrom(oldSlot, oldCls));
        KnowledgeBase kb = runOWLImporter();
        Cls newCls = kb.getCls(oldCls.getName());
        Slot newSlot = kb.getSlot(oldSlot.getName());
        assertSize(1, newCls.getTemplateSlotAllowedClses(newSlot));
        assertEquals(newCls, newCls.getTemplateSlotAllowedClses(newSlot).iterator().next());
    }


    public void testImportHasRestrictionAtStringSlot() {
        OWLNamedClass oldCls = owlModel.createOWLNamedClass("Cls");
        Instance oldInstance = oldCls.createInstance("Instance");
        OWLDatatypeProperty oldSlot = owlModel.createOWLDatatypeProperty("hasValue", owlModel.getXSDstring());
        oldSlot.setDomain(oldCls);
        oldCls.addSuperclass(owlModel.createOWLHasValue(oldSlot, "Value"));
        KnowledgeBase kb = runOWLImporter();
        Cls newCls = kb.getCls(oldCls.getName());
        Slot newSlot = kb.getSlot(oldSlot.getName());
        assertSize(1, newCls.getTemplateSlotValues(newSlot));
        assertEquals("Value", newCls.getTemplateSlotValues(newSlot).iterator().next());
    }


    public void testImportHasRestrictionAtObjectSlot() {
        OWLNamedClass oldCls = owlModel.createOWLNamedClass("Cls");
        Instance oldInstance = oldCls.createInstance("Instance");
        OWLObjectProperty oldSlot = owlModel.createOWLObjectProperty("hasValue");
        oldSlot.setDomain(oldCls);
        oldCls.addSuperclass(owlModel.createOWLHasValue(oldSlot, oldInstance));
        KnowledgeBase kb = runOWLImporter();
        Cls newCls = kb.getCls(oldCls.getName());
        Instance newInstance = kb.getInstance(oldInstance.getName());
        Slot newSlot = kb.getSlot(oldSlot.getName());
        assertSize(1, newCls.getTemplateSlotValues(newSlot));
        assertEquals(newInstance, newCls.getTemplateSlotValues(newSlot).iterator().next());
    }


    public void testImportMaxCardiRestriction() {
        OWLNamedClass oldCls = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty oldSlot = owlModel.createOWLObjectProperty("hasChildren");
        oldSlot.setDomain(oldCls);
        oldCls.addSuperclass(owlModel.createOWLMaxCardinality(oldSlot, 1));
        KnowledgeBase kb = runOWLImporter();
        Cls newCls = kb.getCls(oldCls.getName());
        Slot newSlot = kb.getSlot(oldSlot.getName());
        assertEquals(1, newCls.getTemplateSlotMaximumCardinality(newSlot));
    }


    public void testImportMinCardiRestriction() {
        OWLNamedClass oldCls = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty oldSlot = owlModel.createOWLObjectProperty("hasChildren");
        oldSlot.setDomain(oldCls);
        oldCls.addSuperclass(owlModel.createOWLMinCardinality(oldSlot, 1));
        KnowledgeBase kb = runOWLImporter();
        Cls newCls = kb.getCls(oldCls.getName());
        Slot newSlot = kb.getSlot(oldSlot.getName());
        assertEquals(1, newCls.getTemplateSlotMinimumCardinality(newSlot));
    }
}
