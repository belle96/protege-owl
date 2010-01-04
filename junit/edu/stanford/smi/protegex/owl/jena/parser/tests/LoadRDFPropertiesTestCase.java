package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.jena.protege2jena.Protege2Jena;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadRDFPropertiesTestCase extends AbstractJenaTestCase {

    public void testLoadRDFProperty() throws Exception {
        loadRemoteOntology("rdfSlotTypes.owl");
        RDFProperty anyProperty = owlModel.getRDFProperty("anySlot");
        assertNull(anyProperty.getRange());
        RDFProperty booleanProperty = owlModel.getRDFProperty("booleanSlot");
        assertEquals(owlModel.getXSDboolean(), booleanProperty.getRange());
        RDFProperty stringProperty = owlModel.getRDFProperty("stringSlot");
        assertEquals(owlModel.getXSDstring(), stringProperty.getRange());
        RDFProperty objectProperty = owlModel.getRDFProperty("objectSlot");
        assertTrue(objectProperty.hasObjectRange());
        assertSize(1, objectProperty.getUnionRangeClasses());
        assertEquals(owlModel.getOWLNamedClass("Cls"), objectProperty.getUnionRangeClasses().iterator().next());
    }


    public void testLoadAnnotationProperties() throws Exception {
        // TODO: Test that annot. properties without other types are datatype properties
    }


    public void testLoadFunctionalProperties() throws Exception {
        loadTestOntology(new URI(getRemoteOntologyRoot() + "travel.owl"));
        RDFProperty hasCityProperty = owlModel.getRDFProperty("hasCity");
        Collection types = hasCityProperty.getProtegeTypes();
        assertSize(2, types);
        assertContains(owlModel.getOWLFunctionalPropertyClass(), types);
        assertContains(owlModel.getOWLDatatypePropertyClass(), types);
        assertEquals(owlModel.getOWLDatatypePropertyClass(), types.iterator().next());
        assertTrue(hasCityProperty instanceof OWLDatatypeProperty);

        OWLNamedClass contactClass = owlModel.getOWLNamedClass("Contact");
        Collection properties = contactClass.getUnionDomainProperties();
        assertSize(4, properties);
        for (Iterator it = properties.iterator(); it.hasNext();) {
            RDFProperty property = (RDFProperty) it.next();
            assertTrue(property instanceof OWLDatatypeProperty);
        }

        Protege2Jena.createOntModel(owlModel);
    }
}
