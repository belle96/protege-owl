package edu.stanford.smi.protegex.owl.jena.parser.tests;

import java.net.URI;
import java.net.URL;

import edu.stanford.smi.protegex.owl.repository.impl.HTTPRepository;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadOWLSTestCase extends AbstractJenaTestCase {

    public void testLoadBravoAir() throws Exception {
    	owlModel.getRepositoryManager().addProjectRepository(new HTTPRepository(new URL("http://protege.stanford.edu/junitOntologies/owls/time-entry.owl")));
        loadTestOntology(new URI("http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl"));
    }


    public void testLoadCongo() throws Exception {
    	owlModel.getRepositoryManager().addProjectRepository(new HTTPRepository(new URL("http://protege.stanford.edu/junitOntologies/owls/time-entry.owl")));
        loadTestOntology(new URI("http://www.daml.org/services/owl-s/1.1/CongoProfile.owl"));
    }


    public void testLoadOWLSProcess() throws Exception {
    	owlModel.getRepositoryManager().addProjectRepository(new HTTPRepository(new URL("http://protege.stanford.edu/junitOntologies/owls/time-entry.owl")));
        loadTestOntology(new URI("http://www.daml.org/services/owl-s/1.0/Process.owl"));
    }


    public void testLoadOWLSGrouding() throws Exception {
    	owlModel.getRepositoryManager().addProjectRepository(new HTTPRepository(new URL("http://protege.stanford.edu/junitOntologies/owls/time-entry.owl")));
        loadTestOntology(new URI("http://www.daml.org/services/owl-s/1.0/Grounding.owl"));
    }
}
