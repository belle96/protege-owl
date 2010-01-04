package edu.stanford.smi.protegex.owl.model.util.tests;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.util.XSDVisibility;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Iterator;
import java.util.Set;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class XSDVisibilityTestCase extends AbstractJenaTestCase {

    public void testNewProject() {
        XSDVisibility.updateVisibility(owlModel);
        Set defaultDatatypes = XSDVisibility.getDefaultDatatypes(owlModel);
        Iterator types = owlModel.getRDFSDatatypes().iterator();
        while (types.hasNext()) {
            RDFSDatatype datatype = (RDFSDatatype) types.next();
            if (defaultDatatypes.contains(datatype)) {
                assertTrue(datatype.isVisible());
            }
            else {
                assertFalse(datatype.isVisible());
            }
        }
    }


    public void testUseXSDinteger() {
        RDFSDatatype type = owlModel.getXSDinteger();
        assertFalse(XSDVisibility.getDefaultDatatypes(owlModel).contains(type));
        XSDVisibility.updateVisibility(owlModel);
        assertFalse(type.isVisible());
        RDFProperty property = owlModel.createRDFProperty("property");
        property.setRange(type);
        XSDVisibility.updateVisibility(owlModel);
        assertTrue(type.isVisible());
    }
}
