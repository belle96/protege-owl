package edu.stanford.smi.protegex.owl.model.factory.tests.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.factory.tests.TestPerson;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFIndividual;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultTestPerson extends DefaultRDFIndividual implements TestPerson {

    public DefaultTestPerson(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultTestPerson() {
    }
}
