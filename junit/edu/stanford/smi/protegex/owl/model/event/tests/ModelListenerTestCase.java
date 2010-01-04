package edu.stanford.smi.protegex.owl.model.event.tests;

import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.event.ModelAdapter;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ModelListenerTestCase extends AbstractJenaTestCase {

    private boolean event = false;


    public void testCreateClassEvent() {
        owlModel.addModelListener(new ModelAdapter() {
            public void classCreated(RDFSClass cls) {
                event = true;
            }
        });
        assertFalse(event);
        owlModel.createOWLNamedClass("Test");
        assertTrue(event);
    }
}
