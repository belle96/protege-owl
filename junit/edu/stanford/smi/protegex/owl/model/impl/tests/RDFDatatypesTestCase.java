package edu.stanford.smi.protegex.owl.model.impl.tests;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFDatatypesTestCase extends AbstractJenaTestCase {

    public void testDefaultTypes() {
        Collection types = owlModel.getRDFSDatatypes();
        assertEquals(43, types.size());
        for (Iterator it = types.iterator(); it.hasNext();) {
            RDFSDatatype datatype = (RDFSDatatype) it.next();
            assertTrue(datatype.isSystem());
        }

        RDFSDatatype intType = owlModel.getRDFSDatatypeByName("xsd:int");
        assertEquals(XSDDatatype.XSDint.getURI(), intType.getURI());
        assertEquals(intType, owlModel.getXSDint());

        RDFSDatatype floatType = owlModel.getRDFSDatatypeByURI(XSDDatatype.XSDfloat.getURI());
        assertEquals(XSDDatatype.XSDfloat.getURI(), floatType.getURI());
        assertEquals(floatType, owlModel.getXSDfloat());
    }
}
