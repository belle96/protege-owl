package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLModel;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AnonymousResourceTestCase extends AbstractJenaTestCase {



    public void testIsAnonymous() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        assertFalse(cls.isAnonymous());
        assertFalse(owlModel.isAnonymousResourceName(cls.getName()));
        RDFResource instance = (RDFResource) cls.createInstance(owlModel.getNextAnonymousResourceName());
        assertTrue(instance.isAnonymous());
        assertTrue(owlModel.isAnonymousResourceName(instance.getName()));
    }


    public void testAnonymousClasses() {
        OWLComplementClass complementClass = owlModel.createOWLComplementClass(owlThing);
        assertTrue(owlModel.isAnonymousResourceName(complementClass.getName()));
    }
}
