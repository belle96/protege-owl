package edu.stanford.smi.protegex.owl.jena.graph.tests;

import com.hp.hpl.jena.rdf.model.Model;
import edu.stanford.smi.protegex.owl.jena.graph.JenaModelFactory;
import edu.stanford.smi.protegex.owl.jena.graph.ProtegeGraph;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JenaModelFactoryTestCase extends AbstractJenaTestCase {

    public void testSimpleCreate() {
        Model model = JenaModelFactory.createModel(owlModel);
        assertTrue(model.getGraph() instanceof ProtegeGraph);
    }
}
