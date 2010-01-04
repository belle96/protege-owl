package edu.stanford.smi.protegex.owl.jena.graph.tests;

import com.hp.hpl.jena.vocabulary.OWL;
import edu.stanford.smi.protegex.owl.jena.graph.ProtegePrefixMapping;
import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProtegePrefixMappingTestCase extends AbstractJenaTestCase {

    public void testDefaultOWLModel() {
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        ProtegePrefixMapping mapping = new ProtegePrefixMapping(owlModel, ts);
        assertEquals(ts.getDefaultNamespace(), mapping.getNsPrefixURI(""));
        String owlURI = owlThing.getNamespace();
        assertEquals("owl", mapping.getNsURIPrefix(owlURI));
        assertEquals(owlURI, mapping.getNsPrefixURI("owl"));
    }


    public void testExpandPrefixOWLThing() {
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        ProtegePrefixMapping mapping = new ProtegePrefixMapping(owlModel, ts);
        assertEquals(owlThing.getURI(), mapping.expandPrefix(owlThing.getName()));
    }


    public void testExpandInvalidPrefix() {
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        ProtegePrefixMapping mapping = new ProtegePrefixMapping(owlModel, ts);
        String invalidName = "aldi:Test";
        assertEquals(invalidName, mapping.expandPrefix(invalidName));
    }


    public void testExpandPrefixFromDefaultNamespace() {
        OWLNamedClass resource = owlModel.createOWLNamedClass("Class");
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        ProtegePrefixMapping mapping = new ProtegePrefixMapping(owlModel, ts);
        assertEquals(resource.getURI(), mapping.expandPrefix(resource.getName()));
    }


    public void testShortFormOWLThing() {
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        ProtegePrefixMapping mapping = new ProtegePrefixMapping(owlModel, ts);
        assertEquals(NamespaceUtil.getPrefixedName(owlModel, owlThing.getName()), mapping.shortForm(OWL.Thing.getURI()));
    }


    public void testShortFormInvalid() {
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        ProtegePrefixMapping mapping = new ProtegePrefixMapping(owlModel, ts);
        String invalidName = "http://aldi.de#Test";
        assertEquals(invalidName, mapping.shortForm(invalidName));
    }
}
