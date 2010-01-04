package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateDisjointClassesTestCase extends AbstractJenaCreatorTestCase {

    public void testDisjointClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLNamedClass disjointCls = owlModel.createOWLNamedClass("Disjoint");
        cls.addDisjointClass(disjointCls);
        OntModel ontModel = runJenaCreator();
        Iterator it = ontModel.getOntClass(cls.getURI()).listDisjointWith();
        assertTrue(it.hasNext());
        assertEquals(ontModel.getOntClass(disjointCls.getURI()), it.next());
        assertFalse(it.hasNext());
    }
}
