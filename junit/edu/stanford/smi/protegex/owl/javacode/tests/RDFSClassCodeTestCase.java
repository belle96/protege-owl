package edu.stanford.smi.protegex.owl.javacode.tests;

import edu.stanford.smi.protegex.owl.javacode.ProjectBasedJavaCodeGeneratorOptions;
import edu.stanford.smi.protegex.owl.javacode.RDFPropertyAtClassCode;
import edu.stanford.smi.protegex.owl.javacode.RDFSClassCode;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFSClassCodeTestCase extends AbstractJenaTestCase {

    public void testSimpleRDFSNamedClass() {
        final String NAME = "Test-Class";
        RDFSNamedClass cls = owlModel.createRDFSNamedClass(NAME);
        RDFSClassCode code = new RDFSClassCode(cls, false);
        assertEquals("Test_Class", code.getJavaName());
        assertSize(0, code.getPropertyCodes(false));
    }


    public void testRDFSNamedClassWithSimpleProperty() {
        final String CLASS_NAME = "TestClass";
        final String PROPERTY_NAME = "property";
        RDFSNamedClass cls = owlModel.createRDFSNamedClass(CLASS_NAME);
        RDFProperty property = owlModel.createRDFProperty(PROPERTY_NAME);
        property.setDomain(cls);
        RDFSClassCode code = new RDFSClassCode(cls, false);
        assertEquals(CLASS_NAME, code.getJavaName());
        assertSize(1, code.getPropertyCodes(false));
        RDFPropertyAtClassCode pc = (RDFPropertyAtClassCode) code.getPropertyCodes(false).iterator().next();
        assertEquals(property, pc.getRDFProperty());
    }
}
