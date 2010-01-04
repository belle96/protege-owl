package edu.stanford.smi.protegex.owl.ui.explorer.filter.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.explorer.ExplorerFilter;
import edu.stanford.smi.protegex.owl.ui.explorer.filter.DefaultExplorerFilter;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultExplorerFilterTestCase extends AbstractJenaTestCase {

    public void testAllowAllByDefault() {
        ExplorerFilter filter = new DefaultExplorerFilter();
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        assertTrue(filter.isValidChild(c, owlModel.createOWLNamedClass("Other")));
        assertTrue(filter.isValidChild(c, owlModel.createOWLAllValuesFrom(property, c)));
        assertTrue(filter.isValidChild(c, owlModel.createOWLSomeValuesFrom(property, c)));
        assertTrue(filter.isValidChild(c, owlModel.createOWLHasValue(property, c)));
        assertTrue(filter.isValidChild(c, owlModel.createOWLCardinality(property, 1)));
        assertTrue(filter.isValidChild(c, owlModel.createOWLMaxCardinality(property, 1)));
        assertTrue(filter.isValidChild(c, owlModel.createOWLMinCardinality(property, 1)));
        assertTrue(filter.isValidChild(c, owlModel.createOWLIntersectionClass()));
        assertTrue(filter.isValidChild(c, owlModel.createOWLUnionClass()));
        assertTrue(filter.isValidChild(c, owlModel.createOWLComplementClass(c)));
        assertTrue(filter.isValidChild(c, owlModel.createOWLEnumeratedClass()));
    }


    public void testExistentialTree() {
        DefaultExplorerFilter filter = new DefaultExplorerFilter();
        filter.removeAllValidClasses();
        filter.addValidClass(OWLSomeValuesFrom.class);
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        assertFalse(filter.isValidChild(c, owlModel.createOWLNamedClass("Other")));
        assertFalse(filter.isValidChild(c, owlModel.createOWLAllValuesFrom(property, c)));
        RDFSNamedClass filler = c;
        OWLSomeValuesFrom someValuesFrom = owlModel.createOWLSomeValuesFrom(property, filler);
        assertTrue(filter.isValidChild(c, someValuesFrom));
        assertFalse(filter.isValidChild(c, owlModel.createOWLHasValue(property, c)));
        assertFalse(filter.isValidChild(c, owlModel.createOWLCardinality(property, 1)));
        assertFalse(filter.isValidChild(c, owlModel.createOWLMaxCardinality(property, 1)));
        assertFalse(filter.isValidChild(c, owlModel.createOWLMinCardinality(property, 1)));
        assertFalse(filter.isValidChild(c, owlModel.createOWLIntersectionClass()));
        assertFalse(filter.isValidChild(c, owlModel.createOWLUnionClass()));
        assertFalse(filter.isValidChild(c, owlModel.createOWLComplementClass(c)));
        assertFalse(filter.isValidChild(c, owlModel.createOWLEnumeratedClass()));
        assertTrue(filter.isValidChild(someValuesFrom, filler));
    }


    public void testExistentialTreeWithSubproperty() {
        DefaultExplorerFilter filter = new DefaultExplorerFilter();
        filter.removeAllValidClasses();
        filter.addValidClass(OWLSomeValuesFrom.class);
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        RDFProperty subproperty = owlModel.createSubproperty("sub", property);
        filter.setValidProperty(property);
        OWLSomeValuesFrom someValuesFrom = owlModel.createOWLSomeValuesFrom(subproperty, c);
        assertTrue(filter.isValidChild(c, someValuesFrom));
    }
}
