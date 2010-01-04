package edu.stanford.smi.protegex.owl.ui.clsproperties.tests;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.clsproperties.RDFPropertiesTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFPropertiesTableModelTestCase extends AbstractJenaTestCase {

    public void testSimple() {
        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        RDFProperty propertyA = owlModel.createRDFProperty("a");
        RDFProperty propertyB = owlModel.createRDFProperty("b");
        RDFProperty propertyC = owlModel.createRDFProperty("c");
        propertyB.setDomain(cls);
        propertyA.setDomain(cls);
        propertyC.setDomain(cls);
        RDFPropertiesTableModel tableModel = new RDFPropertiesTableModel();
        tableModel.setClass(cls);
        assertEquals(3, tableModel.getRowCount());
        assertEquals(propertyA, tableModel.getRDFProperty(0));
        assertEquals(propertyB, tableModel.getRDFProperty(1));
        assertEquals(propertyC, tableModel.getRDFProperty(2));
    }
}
