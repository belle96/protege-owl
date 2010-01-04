package edu.stanford.smi.protegex.owl.inference.protegeowl.tests;

import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractDIGReasonerTestCase;

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
public class DisjointTestCase extends AbstractDIGReasonerTestCase {

    private static ProtegeReasoner reasoner; 

    public void testDisjointClassesQuery() {
        try {
            OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
            OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
            clsA.addDisjointClass(clsB);          
            
            ReasonerManager rm = ReasonerManager.getInstance();
            ProtegeReasoner reasoner = rm.createProtegeReasoner(owlModel, rm.getDefaultDIGReasonerClass());
            
            assertTrue(reasoner.isDisjointTo(clsA, clsB));
        }
        catch (ProtegeReasonerException e) {
            fail(e.getMessage());
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }
}

