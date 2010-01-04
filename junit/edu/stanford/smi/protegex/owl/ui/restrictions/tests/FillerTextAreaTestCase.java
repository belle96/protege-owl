package edu.stanford.smi.protegex.owl.ui.restrictions.tests;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.restrictions.FillerTextArea;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FillerTextAreaTestCase extends AbstractJenaTestCase {

    public void testHasValueRestrictionOnDatatypeProperty() throws Throwable {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        FillerTextArea textArea = new FillerTextArea(owlModel, null);
        textArea.setRestrictionProperty(owlModel.getRDFProperty(OWLNames.Slot.HAS_VALUE));
        textArea.setOnProperty(property);
        textArea.checkExpression("Bla bla");
    }


    public void testHasValueRestrictionOnObjectProperty() throws Throwable {
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        FillerTextArea textArea = new FillerTextArea(owlModel, null);
        textArea.setRestrictionProperty(owlModel.getRDFProperty(OWLNames.Slot.HAS_VALUE));
        textArea.setOnProperty(property);
        try {
            textArea.checkExpression("Bla bla");
            assertTrue(false);
        }
        catch (Throwable t) {
        }
    }
}
