package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLQuantifierRestriction;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLAllValuesFromFailedTestCase extends AbstractJenaTestCase {

    public void testCheckFiller() throws Exception {
        OWLDatatypeProperty datatypeProperty = owlModel.createOWLDatatypeProperty("property");
        RDFSDatatype datatype = owlModel.getXSDdouble();
        AbstractOWLQuantifierRestriction.checkFillerText(datatype.getName(), datatypeProperty);
    }
}
