package edu.stanford.smi.protegex.owl.jena.parser.tests;

import java.net.URI;
import java.util.Collection;

import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadImportsTestCase extends AbstractJenaTestCase {

    public void testLoadImports() throws Exception {
        loadRemoteOntology("import-demo.owl");
        URI koalaURI = new URI("http://protege.stanford.edu/plugins/owl/owl-library/koala.owl");
        URI brokenURI = new URI("http://protege.stanford.edu/junitOntologies/testset/koala.owl");
        
        assertEquals("http://protege.stanford.edu/plugins/owl/owl-library/import-demo.owl#",
                     owlModel.getNamespaceManager().getDefaultNamespace());
        OWLNamedClass riqClass = owlModel.getOWLNamedClass("RottnestIslandQuokka");
        assertNotNull(riqClass);
        assertTrue(riqClass.isEditable());
        OWLNamedClass quokkaClass = owlModel.getOWLNamedClass("koala:Quokka");
        assertNotNull(quokkaClass);
        assertTrue(quokkaClass.isIncluded());
        Collection allOntologies = owlModel.getCls(OWLNames.Cls.ONTOLOGY).getDirectInstances();
        Collection imported = owlModel.getDefaultOWLOntology().getImports();
        assertSize(2, allOntologies);
        assertContains(owlModel.getDefaultOWLOntology(), allOntologies);
        assertContains(owlModel.getOWLOntologyByURI(koalaURI), allOntologies);
        
        assertSize(1, imported);
        assertContains(brokenURI.toString(), imported);
        
        assertSize(3, owlModel.getTripleStoreModel().getTripleStores());
        assertNotNull(owlModel.getTripleStoreModel().getTripleStore(koalaURI.toString()));
        
        assertEquals(owlModel.getNamespaceManager().getNamespaceForPrefix("koala"), 
                     koalaURI.toString() + "#");
        assertTrue(owlModel.getOWLOntologyByURI(koalaURI).isIncluded());
        OWLNamedClass koalaCls = owlModel.getOWLNamedClass("koala:Koala");
        assertNotNull(koalaCls);
    }


    public void testLoadUglyImport() throws Exception {
        loadRemoteOntology("uglyImport.owl");
        assertEquals("http://aldi.de/ont/", owlModel.getNamespaceManager().getDefaultNamespace());
        OWLOntology oi = owlModel.getDefaultOWLOntology();
        assertSize(1, oi.getImports());   // the import is broken
        assertNotNull(owlModel.getOWLNamedClass("travel:Sunbathing")); // but the data is imported
    }


    public void testLoadImportTravel() throws Exception {
        loadRemoteOntology("importTravel.owl");
        OWLNamedClass ratingClass = owlModel.getOWLNamedClass("travel:AccommodationRating");
        assertNotNull(ratingClass);
        final RDFSClass definition = ratingClass.getDefinition();
        assertTrue(definition instanceof OWLEnumeratedClass);
        OWLEnumeratedClass enumeratedClass = (OWLEnumeratedClass) definition;
        RDFResource type = enumeratedClass.getRDFType();
        assertEquals(OWLNames.Cls.NAMED_CLASS, type.getName());
    }


    public void testLoadIncrementalImport() throws Exception {
        URI uri = new URI(getRemoteOntologyRoot() + "travel.owl");
        String namespace = uri.toString() + "#";
        owlModel.getNamespaceManager().setPrefix(namespace, "travel");
        owlModel.loadImportedAssertions(uri);
    }


    public void testLoadIncrementalImportOWLS() throws Exception {
        URI uri = new URI("http://www.daml.org/services/owl-s/1.1/Profile.owl");
        String namespace = uri.toString() + "#";
        owlModel.getNamespaceManager().setPrefix(namespace, "profile");
        owlModel.loadImportedAssertions(uri);
        owlModel.getDefaultOWLOntology().addImports(uri);
    }


    public void testLoadIncrementalImportOWLS2() throws Exception {
        {
            URI uri = new URI("http://www.daml.org/services/owl-s/1.1/Process.owl");
            String namespace = uri.toString() + "#";
            owlModel.getNamespaceManager().setPrefix(namespace, "process");
            owlModel.loadImportedAssertions(uri);
            owlModel.getDefaultOWLOntology().addImports(uri.toString());
        }
        {
            URI uri = new URI("http://www.daml.org/services/owl-s/1.1/Grounding.owl");
            owlModel.loadImportedAssertions(uri);
            owlModel.getDefaultOWLOntology().addImports(uri.toString());
        }
    }


    public void testLoadOWLSIndividual() throws Exception {
        loadRemoteOntology("CompositeProcess.owl");
        RDFResource individual = owlModel.getRDFResource("MyIndividual");
        assertTrue(individual instanceof OWLIndividual);
    }


    public void testLoadIncrementalImportOfProtege() throws Exception {
        URI uri = new URI(ProtegeNames.PROTEGE_OWL_ONTOLOGY);
        String namespace = uri.toString() + "#";
        owlModel.getNamespaceManager().setPrefix(namespace, "protege");
        owlModel.loadImportedAssertions(uri);
        owlModel.getDefaultOWLOntology().addImports(uri.toString());
        assertNotNull(owlModel.getOWLNamedClass("protege:PAL-CONSTRAINT"));
    }
}
