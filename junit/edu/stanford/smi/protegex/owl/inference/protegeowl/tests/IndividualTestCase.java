package edu.stanford.smi.protegex.owl.inference.protegeowl.tests;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.inference.dig.reasoner.DIGReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jun 2, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 *
 * @prowl.junit.dig
 */
public class IndividualTestCase extends AbstractProtegeOwlTestCase {
    private static transient final Logger log = Log.getLogger(IndividualTestCase.class);
    
    
    static void enableDebugging(Level level) {
        DIGReasoner.digLogger.setLevel(level);
        log.setLevel(level);
    }

  
    public void testTypesQuery() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            OWLIndividual indA = clsA.createOWLIndividual("iA");
            OWLIndividual indThing = owlModel.getOWLThingClass().createOWLIndividual("iThing");
            indA.addRDFType(clsB);

            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            Collection iATypes = reasoner.getIndividualTypes(indA);
            assertTrue(iATypes.contains(clsA));
            assertTrue(iATypes.contains(clsB));
            Collection thingTypes = reasoner.getIndividualTypes(indThing);
            assertTrue(thingTypes.contains(owlModel.getOWLThingClass()));
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }


    public void testIndividualsBelongingToClassQuery() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            OWLIndividual indA = clsA.createOWLIndividual("iA");
            OWLIndividual indThing = owlModel.getOWLThingClass().createOWLIndividual("iThing");
            indA.addRDFType(clsB);

            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            assertTrue(reasoner.getIndividualsBelongingToClass(clsA).contains(indA));
            assertTrue(reasoner.getIndividualsBelongingToClass(clsB).contains(indA));
            assertTrue(reasoner.getIndividualsBelongingToClass(owlModel.getOWLThingClass()).contains(indThing));
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }
}

