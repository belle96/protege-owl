package edu.stanford.smi.protegex.owl.model.triplestore.impl.tests;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.triplestore.Triple;
import edu.stanford.smi.protegex.owl.model.triplestore.impl.DefaultTriple;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultTripleTestCase extends AbstractJenaTestCase {

    public void testCreateTriple() {
        RDFResource subject = owlModel.createRDFSNamedClass("Class");
        RDFProperty property = owlModel.createRDFProperty("property");
        Object object = "Value";
        Triple triple = new DefaultTriple(subject, property, object);
        assertEquals(subject, triple.getSubject());
        assertEquals(property, triple.getPredicate());
        assertEquals(object, triple.getObject());
    }
}
