package edu.stanford.smi.protegex.owl.ui.editors.tests;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.editors.PropertyValueEditor;
import edu.stanford.smi.protegex.owl.ui.editors.StringValueEditor;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class StringValueTestCase extends AbstractJenaTestCase {

    public void testStringProperty() {
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property");
        property.setRange(owlModel.getXSDstring());
        PropertyValueEditor editor = new StringValueEditor();
        assertTrue(editor.canEdit(owlThing, property, null));
        assertTrue(editor.canEdit(owlThing, property, "Test"));
        assertTrue(editor.canEdit(owlThing, property, owlModel.createRDFSLiteral("Test", "de")));
    }


    public void testUnrestrictedProperty() {
        RDFProperty property = owlModel.createRDFProperty("property");
        PropertyValueEditor editor = new StringValueEditor();
        assertFalse(editor.canEdit(owlThing, property, null));
        assertTrue(editor.canEdit(owlThing, property, "Test"));
    }
}
