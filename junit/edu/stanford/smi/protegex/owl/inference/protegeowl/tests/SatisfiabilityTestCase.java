package edu.stanford.smi.protegex.owl.inference.protegeowl.tests;

import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jun 1, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 *
 * @prowl.junit.dig
 */
public class SatisfiabilityTestCase extends AbstractProtegeOwlTestCase {


    public void testOWLThingSatisfiableQuery() {
        try {           
            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            assertTrue(reasoner.isSatisfiable(owlModel.getOWLThingClass()));
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }


    public void testOWLNothingSatisfiableQuery() {
        try {
            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            assertFalse(reasoner.isSatisfiable(owlModel.getOWLNothing()));
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }


    public void testConceptSatisfiabilityQuery() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
            clsC.addSuperclass(clsA);
            clsC.addSuperclass(clsB);
           
            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            assertTrue(reasoner.isSatisfiable(clsC));
            clsA.addDisjointClass(clsB);
            assertFalse(reasoner.isSatisfiable(clsC));
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }


    public void testIntersectionSatisfiableQuery() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
            
            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
                        
            OWLClass [] clses = new OWLClass []{clsA, clsB, clsC};
            assertTrue(reasoner.isIntersectionSatisfiable(clses));
            clsA.addDisjointClass(clsB);
            assertFalse(reasoner.isIntersectionSatisfiable(clses));
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }


}

