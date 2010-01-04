package edu.stanford.smi.protegex.owl.ui.widget.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.widget.AbstractPropertyValuesWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class WidgetConstraintViolationsTestCase extends AbstractJenaTestCase {

    public void testMaxCardinality() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        RDFProperty predicate = owlModel.createOWLDatatypeProperty("property");
        RDFResource subject = cls.createInstance("instance");
        assertFalse(AbstractPropertyValuesWidget.isInvalid(subject, predicate, Collections.EMPTY_LIST));
        cls.addSuperclass(owlModel.createOWLMaxCardinality(predicate, 1));
        List values = new ArrayList();
        assertFalse(AbstractPropertyValuesWidget.isInvalid(subject, predicate, values));
        values.add("A");
        assertFalse(AbstractPropertyValuesWidget.isInvalid(subject, predicate, values));
        values.add("B");
        assertTrue(AbstractPropertyValuesWidget.isInvalid(subject, predicate, values));
    }


    public void testMinCardinality() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        RDFProperty predicate = owlModel.createOWLDatatypeProperty("property");
        RDFResource subject = cls.createInstance("instance");
        assertFalse(AbstractPropertyValuesWidget.isInvalid(subject, predicate, Collections.EMPTY_LIST));
        cls.addSuperclass(owlModel.createOWLMinCardinality(predicate, 2));
        List values = new ArrayList();
        assertTrue(AbstractPropertyValuesWidget.isInvalid(subject, predicate, values));
        values.add("A");
        assertTrue(AbstractPropertyValuesWidget.isInvalid(subject, predicate, values));
        values.add("B");
        assertFalse(AbstractPropertyValuesWidget.isInvalid(subject, predicate, values));
    }
}
