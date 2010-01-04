package edu.stanford.smi.protegex.owl.ui.restrictions.tests;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.restrictions.RestrictionEditorPanel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RestrictionEditorPanelTestCase extends AbstractJenaTestCase {

    public void testCreatePanel() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        Cls someValuesFromCls = owlModel.getCls(OWLNames.Cls.ALL_VALUES_FROM_RESTRICTION);
        RestrictionEditorPanel panel = new RestrictionEditorPanel(owlModel,
                someValuesFromCls, null, null, cls);
        assertNotNull(panel);
    }
}
