package edu.stanford.smi.protegex.owl.jena.triplestore.tests;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class GetPropertyValuesTestCase extends AbstractJenaTestCase {

    public void testAddValues() {
        RDFResource resource = owlModel.getOWLThingClass();
        RDFProperty property = owlModel.getOWLVersionInfoProperty();
        String value = "1.1";
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        ts.add(resource, property, value);
        Collection values = owlModel.getTripleStoreModel().getPropertyValues(resource, property);
        assertSize(1, values);
        assertContains(value, values);
    }


    public void testAddRDFSLiteral() {
        RDFResource resource = owlModel.getOWLThingClass();
        RDFProperty property = owlModel.getOWLVersionInfoProperty();
        String value = "1.1";
        String lang = "de";
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        RDFSLiteral oldLiteral = owlModel.createRDFSLiteral(value, lang);
        ts.add(resource, property, oldLiteral);
        Collection values = owlModel.getTripleStoreModel().getPropertyValues(resource, property);
        assertSize(1, values);
        RDFSLiteral newLiteral = (RDFSLiteral) values.iterator().next();
        assertEquals(value, newLiteral.getString());
        assertEquals(lang, newLiteral.getLanguage());
    }
}
