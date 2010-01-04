package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collections;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SynonymSearchFailedTestCase extends AbstractJenaTestCase {

    public void testSetSynonymSlots() {
        assertSize(0, owlModel.getSearchSynonymProperties());
        OWLDatatypeProperty anno = owlModel.createAnnotationOWLDatatypeProperty("anno");
        owlModel.setSearchSynonymProperties(Collections.singleton(anno));
        assertSize(1, owlModel.getSearchSynonymProperties());
        assertEquals(anno, owlModel.getSearchSynonymProperties().iterator().next());
        owlModel.setSearchSynonymProperties(Collections.EMPTY_LIST);
        assertSize(0, owlModel.getSearchSynonymProperties());
    }


    public void testSearchSynonymSlot() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLDatatypeProperty anno = owlModel.createAnnotationOWLDatatypeProperty("anno");
        final String value = "aldi";
        cls.setPropertyValue(anno, value);
        assertSize(0, owlModel.getFrameNameMatches(value, 100));
        owlModel.setSearchSynonymProperties(Collections.singleton(anno));
        //TT - invalid test, standard search does not use this property
        //assertSize(1, owlModel.getFrameNameMatches(value, 100)); 
        //assertEquals(cls, owlModel.getFrameNameMatches(value, 100).iterator().next());
    }
}
