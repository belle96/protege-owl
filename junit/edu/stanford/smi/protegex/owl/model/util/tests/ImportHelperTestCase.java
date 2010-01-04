package edu.stanford.smi.protegex.owl.model.util.tests;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import edu.stanford.smi.protegex.owl.model.util.ImportHelper;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

public class ImportHelperTestCase extends AbstractJenaTestCase {
    
    public static final String koalaPrefix = "http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#";
    public static final String travelPrefix = "http://www.owl-ontologies.com/travel.owl#";
    
    private static final URI travelUri = getRemoteOntologyURI("JunitTravel.owl");
    private static final URI koalaUri = getRemoteOntologyURI("JunitKoala.owl");
    
    public void testImportHelper00() throws IOException, InterruptedException {
        try {
            ImportHelper helper = new ImportHelper(owlModel);
            
            helper.addImport(travelUri);
            helper.addImport(koalaUri);
            
            helper.importOntologies();
            
            checkOntology();
        } 
        catch (Throwable t) {
            fail();
        }
    }
    
    public void testImportHelper01() throws IOException, InterruptedException {
        try {
            ImportHelper helper = new ImportHelper(owlModel);
            
            helper.addImport(travelUri);
            helper.addImport(koalaUri.toURL().openStream());
            
            helper.importOntologies();
            
            checkOntology();  
        } 
        catch (Throwable t) {
            fail();
        }
    }
    
    public void testImportHelper10() throws IOException, InterruptedException {
        try {
            ImportHelper helper = new ImportHelper(owlModel);
            
            helper.addImport(travelUri.toURL().openStream());
            helper.addImport(koalaUri);
            
            helper.importOntologies();
            
            checkOntology();  
        } 
        catch (Throwable t) {
            fail();
        }
    }
    
    
    public void testImportHelper11() throws IOException, InterruptedException {
        try {
            ImportHelper helper = new ImportHelper(owlModel);
            
            helper.addImport(travelUri.toURL().openStream());
            helper.addImport(koalaUri.toURL().openStream());
            
            helper.importOntologies();
            
            checkOntology();     
        } 
        catch (Throwable t) {
            fail();
        }
    }
    
    private void checkOntology() {
        assertNotNull(owlModel.getOWLNamedClass(koalaPrefix + "Habitat"));
        assertNotNull(owlModel.getOWLNamedClass(travelPrefix + "Destination"));
        assertEquals(3, owlModel.getOWLOntologyClass().getInstances(false).size());
        assertTrue(owlModel.getOWLOntologyClass().getInstances(false).contains(owlModel.getOWLOntologyByURI(travelUri)));
        assertTrue(owlModel.getOWLOntologyClass().getInstances(false).contains(owlModel.getOWLOntologyByURI(koalaUri)));
        assertEquals(2, owlModel.getDefaultOWLOntology().getImports().size());
        assertTrue(owlModel.getDefaultOWLOntology().getImports().contains(travelUri.toString()));
        assertTrue(owlModel.getDefaultOWLOntology().getImports().contains(koalaUri.toString()));  
    }

}
