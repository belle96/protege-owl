package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLCardinality;
import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLCardinalityTestCase extends AbstractJenaTestCase {

    public void testPlainBrowserText() {
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        OWLCardinality cardinality = owlModel.createOWLCardinality(property, 4);
        assertEquals("property exactly 4", cardinality.getBrowserText());
    }


    public void testQualifiedBrowserText() {
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        OWLCardinality cardinality = owlModel.createOWLCardinality(property, 4);
        cardinality.setValuesFrom(owlThing);
        assertEquals("property exactly 4 owl:Thing", cardinality.getBrowserText());
        OWLComplementClass complementClass = owlModel.createOWLComplementClass(owlThing);
        cardinality.setValuesFrom(complementClass);
        assertEquals("property exactly 4 not owl:Thing", cardinality.getBrowserText());
    }
}
