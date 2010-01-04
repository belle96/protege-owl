package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.RDFUntypedResource;
import edu.stanford.smi.protegex.owl.model.factory.AlreadyImportedException;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLOntologyTestCase extends AbstractJenaTestCase {

    public void testImportsByURIString() throws AlreadyImportedException {
        OWLOntology ontology = owlModel.createOWLOntology("owl");
        assertSize(0, ontology.getImports());
        String importURI = "http://aldi.de/ontology.owl";
        ontology.addImports(importURI);
        assertSize(1, ontology.getImports());
        assertContains(importURI, ontology.getImports());
        assertSize(1, ontology.getImportResources());
        Object first = ontology.getImportResources().iterator().next();
        assertTrue(first instanceof RDFUntypedResource);
        assertEquals(importURI, ((RDFUntypedResource) first).getURI());
        ontology.removeImports(importURI);
        assertSize(0, ontology.getImports());
    }
}
