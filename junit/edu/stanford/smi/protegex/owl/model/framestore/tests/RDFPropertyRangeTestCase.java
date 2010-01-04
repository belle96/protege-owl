package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFPropertyRangeTestCase extends AbstractJenaTestCase {

    public void testUnionRange() {
        RDFSNamedClass classA = owlModel.createRDFSNamedClass("A");
        RDFSNamedClass classB = owlModel.createRDFSNamedClass("B");
        OWLUnionClass unionClass = owlModel.createOWLUnionClass();
        unionClass.addOperand(classA);
        unionClass.addOperand(classB);
        RDFProperty property = owlModel.createRDFProperty("property");
        property.setRange(unionClass);
        assertEquals(ValueType.INSTANCE, ((Slot) property).getValueType());
        assertSize(2, ((Slot) property).getAllowedClses());
        assertContains(classA, ((Slot) property).getAllowedClses());
        assertContains(classB, ((Slot) property).getAllowedClses());
    }

    /*public void testDeleteAnonymousRDFSDatatype() throws Exception {
       loadRemoteOntologyWithProtegeMetadataOntology();
       RDFProperty property = owlModel.createOWLDatatypeProperty("property");
       int oldCount = owlModel.getRDFSDatatypeClass().getInstances(true).size();
       RDFSDatatype datatype = owlModel.createRDFSDatatype(owlModel.getNextAnonymousResourceName());
       property.setRange(datatype);
       int newCount = owlModel.getRDFSDatatypeClass().getInstances(true).size();
       property.setRange(null);
       assertEquals(oldCount, newCount);
   } */
}
