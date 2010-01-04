package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadOWLNothingTestCase extends AbstractJenaTestCase {

    public void testLoadNothing() throws Exception {

        loadRemoteOntology("nothing.owl");
        OWLNamedClass nothingCls = owlModel.getOWLNothing();

        OWLNamedClass cls = owlModel.getOWLNamedClass("Cls");
        Iterator it = cls.getSuperclasses(false).iterator();
        assertEquals(nothingCls, it.next());
    }

    //public void testLoadOpenCyc() throws Exception {
    //    loadTestOntologyWithJenaLoader(new URI("http://www.cyc.com/2003/04/01/cyc"));
    //}
}
