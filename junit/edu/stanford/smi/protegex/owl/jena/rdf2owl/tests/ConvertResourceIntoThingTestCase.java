package edu.stanford.smi.protegex.owl.jena.rdf2owl.tests;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import edu.stanford.smi.protegex.owl.jena.rdf2owl.RDF2OWL;
import junit.framework.TestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConvertResourceIntoThingTestCase extends TestCase {

    public void testConvertRDFSClassToOWLClass() {
        Model model = ModelFactory.createDefaultModel();
        Resource c = model.createResource("http://a.com/onto#Class");
        model.add(c, RDF.type, RDFS.Class);
        model.add(c, RDFS.subClassOf, RDFS.Resource);
        new RDF2OWL(model).run();
        assertTrue(c.hasProperty(RDFS.subClassOf, OWL.Thing));
        assertFalse(c.hasProperty(RDFS.subClassOf, RDFS.Resource));
    }
}
