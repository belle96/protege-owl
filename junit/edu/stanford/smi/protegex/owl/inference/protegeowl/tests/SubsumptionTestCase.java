package edu.stanford.smi.protegex.owl.inference.protegeowl.tests;

import java.util.Collection;
import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
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
public class SubsumptionTestCase extends AbstractProtegeOwlTestCase {
  


    public void testSubsumptionQuery() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            clsB.addEquivalentClass(clsA);
            
            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            // clsA should subsume clsB
            assertTrue(reasoner.isSubsumedBy(clsA, clsB));
        }
        catch (Exception e) {
            fail(e.getMessage());
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }


    public void testSuperclassesQuery() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            clsB.addSuperclass(clsA);
            
            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            assertTrue(reasoner.getSuperclasses(clsB).contains(clsA));
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }


    public void testAncestorClassesQuery() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
            clsB.addSuperclass(clsA);
            clsC.addSuperclass(clsB);
            
            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            Collection ancestorClasses = reasoner.getAncestorClasses(clsC);
            assertTrue(ancestorClasses.contains(clsA));
            assertTrue(ancestorClasses.contains(clsB));
            assertTrue(ancestorClasses.contains(owlModel.getOWLThingClass()));
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }


    public void testSubclassesQuery() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            clsB.addSuperclass(clsA);

            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            assertTrue(reasoner.getSubclasses(clsA).contains(clsB));
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }


    public void testDescendantClassesQuery() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
            clsB.addSuperclass(clsA);
            clsC.addSuperclass(clsB);
            
            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            Collection descendantClasses = reasoner.getDescendantClasses(owlModel.getOWLThingClass());
            assertTrue(descendantClasses.contains(clsA));
            assertTrue(descendantClasses.contains(clsB));
            assertTrue(descendantClasses.contains(clsC));
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }


    public void testSumbsumptionRelationshipQuery() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
            clsB.addSuperclass(clsA);

            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeOWLReasoner reasoner = (ProtegeOWLReasoner) rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            int relationship;
            relationship = reasoner.getSubsumptionRelationship(clsA, clsB, null);
            assertEquals(relationship, ProtegeOWLReasoner.CLS1_SUBSUMES_CLS2);
            relationship = reasoner.getSubsumptionRelationship(clsB, clsA, null);
            assertEquals(relationship, ProtegeOWLReasoner.CLS1_SUBSUMED_BY_CLS2);
            // Now make clsA and clsB equivalent
            clsA.addSuperclass(clsB);
            relationship = reasoner.getSubsumptionRelationship(clsA, clsB, null);
            assertEquals(relationship, ProtegeOWLReasoner.CLS1_EQUIVALENT_TO_CLS2);
            relationship = reasoner.getSubsumptionRelationship(clsC, clsA, null);
            assertEquals(relationship, ProtegeOWLReasoner.NO_SUBSUMPTION_RELATIONSHIP);
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }


    public void testGetSuperclassesOfIntersection() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            
            ProtegeOWLReasoner reasoner = ReasonerManager.getInstance().createReasoner(owlModel);
          
            Collection superClses = reasoner.getSuperclassesOfIntersection(new OWLClass []{clsA, clsB}, null);
            assertTrue(superClses.contains(clsA));
            assertTrue(superClses.contains(clsB));
        }
        catch (DIGReasonerException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }


}

