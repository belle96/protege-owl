package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLAllValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DatatypeEnumerationTestCase extends AbstractJenaTestCase {

    public void testAllValuesFromWithStringDataRange() {
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDstring());
        OWLAllValuesFrom restriction = owlModel.createOWLAllValuesFrom(slot,
                new RDFSLiteral[]{
                        owlModel.createRDFSLiteral("A"),
                        owlModel.createRDFSLiteral("B")
                });
        assertEquals("owl:oneOf{\"A\" \"B\"}", restriction.getFillerText());
    }


    public void testAllValuesFromWithFloatDataRange() {
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDfloat());
        OWLAllValuesFrom restriction = owlModel.createOWLAllValuesFrom(slot,
                new RDFSLiteral[]{
                        owlModel.createRDFSLiteral(new Float(1.1)),
                        owlModel.createRDFSLiteral(new Float(2.2))
                });
        assertEquals("owl:oneOf{1.1 2.2}", restriction.getFillerText());
    }


    public void testAllValuesFromWithIntegerDataRange() {
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDint());
        OWLAllValuesFrom restriction = owlModel.createOWLAllValuesFrom(slot,
                new RDFSLiteral[]{
                        owlModel.createRDFSLiteral(new Integer(1)),
                        owlModel.createRDFSLiteral(new Integer(2))
                });
        assertEquals("owl:oneOf{1 2}", restriction.getFillerText());
    }
}
