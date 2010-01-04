package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLEnumeratedClassTestCase extends AbstractJenaTestCase {

    public void testCreateEnumeratedClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLIndividual a = cls.createOWLIndividual("a");
        OWLIndividual b = cls.createOWLIndividual("b");
        OWLEnumeratedClass enumeratedClass = owlModel.createOWLEnumeratedClass();
        assertNull(enumeratedClass.getPropertyValue(owlModel.getOWLOneOfProperty()));
        enumeratedClass.addOneOf(a);
        enumeratedClass.addOneOf(b);
        assertSize(2, enumeratedClass.getOneOf());
        assertContains(a, enumeratedClass.getOneOf());
        assertContains(b, enumeratedClass.getOneOf());
        assertSize(1, enumeratedClass.getPropertyValues(owlModel.getOWLOneOfProperty()));
        RDFList list = (RDFList) enumeratedClass.getPropertyValue(owlModel.getOWLOneOfProperty());
        assertEquals(a, list.getFirst());
        assertEquals(b, list.getRest().getFirst());
        assertSize(2, list.getValues());
    }
}
