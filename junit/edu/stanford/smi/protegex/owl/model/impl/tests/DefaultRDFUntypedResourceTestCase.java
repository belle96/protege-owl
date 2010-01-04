package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.RDFUntypedResource;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultRDFUntypedResourceTestCase extends AbstractJenaTestCase {

    public void testCreateUntypedResource() {
        String uri = "http://aldi.de";
        RDFUntypedResource resource = owlModel.createRDFUntypedResource(uri);
        assertNotNull(resource);
        assertTrue(owlModel.isValidFrameName(uri, resource));
        assertEquals(uri, resource.getName());
        assertEquals(uri, resource.getURI());
        assertNull(resource.getRDFType());
    }


    public void testCreateViaClass() {
        RDFUntypedResource resource = (RDFUntypedResource) owlModel.getRDFUntypedResourcesClass().createInstance(null);
        assertTrue(owlModel.isValidFrameName("http://aldi.de", resource));
        assertFalse(owlModel.isValidFrameName("  http://aldi.de  ", resource));
        assertFalse(owlModel.isValidFrameName("", resource));
        assertFalse(owlModel.isValidFrameName("urn:", resource));
    }
}
