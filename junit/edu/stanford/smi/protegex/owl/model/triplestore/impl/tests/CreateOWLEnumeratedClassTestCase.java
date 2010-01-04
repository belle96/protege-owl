package edu.stanford.smi.protegex.owl.model.triplestore.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFList;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateOWLEnumeratedClassTestCase extends AbstractTripleStoreTestCase {

    public void testCreateOWLEnumeratedClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLIndividual a = cls.createOWLIndividual("a");
        OWLIndividual b = cls.createOWLIndividual("b");
        OWLEnumeratedClass enumClass = owlModel.createOWLEnumeratedClass();
        RDFList first = owlModel.createRDFList();
        RDFList second = owlModel.createRDFList();
        ts.add(enumClass, owlModel.getOWLOneOfProperty(), first);
        ts.add(first, owlModel.getRDFFirstProperty(), a);
        ts.add(first, owlModel.getRDFRestProperty(), second);
        ts.add(second, owlModel.getRDFFirstProperty(), b);
        ts.add(second, owlModel.getRDFRestProperty(), owlModel.getRDFNil());
        assertSize(2, enumClass.getOneOf());
        assertContains(a, enumClass.getOneOf());
        assertContains(b, enumClass.getOneOf());
    }
}
