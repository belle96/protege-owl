package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateDisjointClassesTestCase extends AbstractProtege2JenaTestCase {

    public void testDisjointClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLNamedClass disjointCls = owlModel.createOWLNamedClass("Disjoint");
        cls.addDisjointClass(disjointCls);
        OntModel ontModel = createOntModel();
        Iterator it = ontModel.getOntClass(cls.getURI()).listDisjointWith();
        assertTrue(it.hasNext());
        assertEquals(ontModel.getOntClass(disjointCls.getURI()), it.next());
        assertFalse(it.hasNext());
    }
}
