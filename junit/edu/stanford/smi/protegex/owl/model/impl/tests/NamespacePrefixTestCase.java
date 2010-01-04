package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.factory.AlreadyImportedException;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.metadatatab.OntologyURIPanel;

/**
 * Test cases for the Protege-only part of the namespace support.
 *
 * @author Holger Knublauch
 */
public class NamespacePrefixTestCase extends AbstractJenaTestCase {

    public void testDefaultOntologyName() throws Exception {
        OWLOntology ontology = owlModel.getDefaultOWLOntology();
        assertTrue(ontology.getName().startsWith(OntologyURIPanel.DEFAULT_BASE));
        assertEquals(ontology.getName(), ontology.getURI());
    }


    public void testOtherOntologyName() throws AlreadyImportedException {
        String namespaceBase = "http://aldi.de";
        String namespace = namespaceBase + "#";
        String prefix = "aldi";
        owlModel.getNamespaceManager().setPrefix(namespace, prefix);
        OWLOntology ontology = owlModel.createOWLOntology(namespaceBase);
        assertEquals(namespaceBase, ontology.getURI());
    }


    public void testDefaultPrefix() {
        NamespaceManager nsm = owlModel.getNamespaceManager();

        final String CLS_NAME = "Person";
        OWLNamedClass cls = owlModel.createOWLNamedClass(CLS_NAME);
        assertEquals(nsm.getDefaultNamespace(), cls.getNamespace());
        assertEquals(nsm.getDefaultNamespace() + CLS_NAME, cls.getURI());
        assertEquals(CLS_NAME, cls.getLocalName());
        assertEquals(CLS_NAME, cls.getBrowserText());
    }


    public void testOntologyInstanceValues() {
        NamespaceManager nsm = owlModel.getNamespaceManager();
        assertEquals(owlModel.getDefaultOWLOntology().getName() + "#", nsm.getDefaultNamespace());

        final String CLS_NAME = "Person";
        OWLNamedClass cls = owlModel.createOWLNamedClass(CLS_NAME);

        String NAMESPACE = "http://aldi.de#";
        String ALDI = "aldi";
        nsm.setPrefix(NAMESPACE, ALDI);

        OWLNamedClass otherCls = owlModel.createOWLNamedClass(ALDI + ":" + CLS_NAME);
        assertEquals(CLS_NAME, otherCls.getLocalName());
        assertEquals(NAMESPACE, otherCls.getNamespace());
    }
}
