package edu.stanford.smi.protegex.owl.writer.rdfxml.util.tests;

import edu.stanford.smi.protegex.owl.jena.writersettings.JenaWriterSettings;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.writer.rdfxml.util.ProtegeWriterSettings;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProtegeWriterSettingsTestCase extends AbstractJenaTestCase {

    public void testSetSettings() {
        assertTrue(owlModel.getWriterSettings() instanceof JenaWriterSettings);
        owlModel.setWriterSettings(new ProtegeWriterSettings(owlModel));
        assertTrue(owlModel.getWriterSettings() instanceof ProtegeWriterSettings);
        ProtegeWriterSettings settings = (ProtegeWriterSettings) owlModel.getWriterSettings();
        assertTrue(settings.getUseXMLEntities());
        assertFalse(settings.isSortAlphabetically());
        settings.setUseXMLEntities(false);
        assertFalse(settings.getUseXMLEntities());
        settings.setSortAlphabetically(true);
        assertTrue(settings.isSortAlphabetically());
    }
}
