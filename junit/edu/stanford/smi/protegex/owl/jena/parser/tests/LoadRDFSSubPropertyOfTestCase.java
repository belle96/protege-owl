package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadRDFSSubPropertyOfTestCase extends AbstractJenaTestCase {


    public void testLoadSubRDFProperty() throws Exception {
        loadRemoteOntology("subRDFProperty.owl");
        RDFProperty superProperty = owlModel.getRDFProperty("superProperty");
        RDFProperty subProperty = owlModel.getRDFProperty("subProperty");
        assertNotNull(superProperty);
        assertSize(1, subProperty.getSuperproperties(false));
        assertContains(superProperty, subProperty.getSuperproperties(false));
    }


    public void testLoadSubpropertyRange() throws Exception {
        loadRemoteOntology("subPropertyRange.owl");
        OWLNamedClass personCls = owlModel.getOWLNamedClass("Person");
        OWLObjectProperty superproperty = owlModel.getOWLObjectProperty("hasChild");
        OWLObjectProperty subproperty = owlModel.getOWLObjectProperty("hasSon");
        assertSize(1, superproperty.getUnionRangeClasses());
        assertEquals(personCls, superproperty.getRange());
        assertEquals(personCls, subproperty.getRange(true));
        assertContains(personCls, superproperty.getUnionRangeClasses());
        assertEquals(null, ((Slot) subproperty).getDirectOwnSlotValue(owlModel.getSlot(Model.Slot.VALUE_TYPE)));
        assertSize(1, subproperty.getUnionRangeClasses());
        assertContains(personCls, subproperty.getUnionRangeClasses().iterator());
    }

    //public void testLoadExternalSuperproperty() throws Exception {
    //    loadRemoteOntologyWithJenaLoader("externalSuperproperty.owl");
    //    RDFProperty subProperty = owlModel.getRDFProperty("subProperty");
    //    assertEquals(1, subProperty.getSuperpropertyCount());
    //    final Frame frame = owlModel.getFrame("superProperty");
    //    assertTrue(frame instanceof RDFProperty);
    //}
}
