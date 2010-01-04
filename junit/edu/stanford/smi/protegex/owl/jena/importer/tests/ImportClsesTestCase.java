package edu.stanford.smi.protegex.owl.jena.importer.tests;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImportClsesTestCase extends AbstractOWLImporterTestCase {

    public void testImportAbstractNamedCls() throws Exception {
        loadRemoteOntologyWithProtegeMetadataOntology();
        OWLNamedClass oldCls = owlModel.createOWLNamedClass("Cls");
        ((Cls) oldCls).setAbstract(true);
        KnowledgeBase kb = runOWLImporter();
        Cls newCls = kb.getCls(oldCls.getName());
        assertTrue(newCls.isAbstract());
    }


    public void testImportSimpleOWLNamedClass() {
        OWLNamedClass oldCls = (OWLNamedClass) owlModel.createOWLNamedSubclass("Cls", owlModel.getOWLThingClass());
        KnowledgeBase kb = runOWLImporter();
        Cls newCls = kb.getCls(oldCls.getName());
        assertNotNull(newCls);
        assertEquals(1, newCls.getDirectSuperclassCount());
        assertEquals(kb.getRootCls(), newCls.getDirectSuperclasses().iterator().next());
    }


    public void testImportSuperclasses() {
        OWLNamedClass parentCls = owlModel.createOWLNamedSubclass("ParentCls", owlModel.getOWLThingClass());
        OWLNamedClass childCls = owlModel.createOWLNamedSubclass("ChildCls", parentCls);
        KnowledgeBase kb = runOWLImporter();
        Cls newParentCls = kb.getCls(parentCls.getName());
        assertEquals(1, newParentCls.getDirectSuperclassCount());
        assertEquals(kb.getRootCls(), newParentCls.getDirectSuperclasses().iterator().next());
        Cls newChildCls = kb.getCls(childCls.getName());
        assertEquals(1, newChildCls.getDirectSuperclassCount());
        assertEquals(newParentCls, newChildCls.getDirectSuperclasses().iterator().next());
    }


    public void testImportTemplateSlots() {
        OWLNamedClass oldClass = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty objectSlot = owlModel.createOWLObjectProperty("objectSlot");
        OWLDatatypeProperty datatypeSlot = owlModel.createOWLDatatypeProperty("datatypeSlot", owlModel.getXSDstring());
        objectSlot.setDomain(oldClass);
        datatypeSlot.setDomain(oldClass);
        KnowledgeBase kb = runOWLImporter();
        Cls newCls = kb.getCls(oldClass.getName());
        assertSize(2, newCls.getDirectTemplateSlots());
        assertContains(kb.getSlot(objectSlot.getName()), newCls.getDirectTemplateSlots());
        assertContains(kb.getSlot(datatypeSlot.getName()), newCls.getDirectTemplateSlots());
    }
}
