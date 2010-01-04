package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.tests;

import java.net.URI;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 9, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RDFXMLWriterOntologyFailedTestCase extends AbstractRDFXMLWriterTestCases {

    public void testKAOntology() {
        doOntology("http://protege.cim3.net/file/pub/ontologies/ka/ka.owl");
    }


    public void testPizzaOntology() {
        doOntology("http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl");
    }


    public void testTravelOntology() {
        doOntology("http://protege.stanford.edu/junitOntologies/testset/travel.owl");
    }


    public void testGenerationsOntology() {
        doOntology("http://protege.cim3.net/file/pub/ontologies/generations/generations.owl");
    }


    public void testKoalaOntology() {
        doOntology("http://protege.cim3.net/file/pub/ontologies/koala/koala.owl");
    }


    public void testCountriesOntology() {
        doOntology("http://www.bpiresearch.com/BPMO/2004/03/03/cdl/Countries");
    }


    public void testMGEDOntology() {
        doOntology("http://mged.sourceforge.net/ontologies/MGEDOntology.owl");
    }


    private void doOntology(String uri) {
        try {
            loadTestOntology(new URI(uri));
            doCheck();
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }
}

