package edu.stanford.smi.protegex.owl.writer.rdfxml.util;

import edu.stanford.smi.protegex.owl.jena.writersettings.WriterSettings;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProtegeWriterSettings implements WriterSettings {

    private OWLModel owlModel;

    public final static String SORT_ALPHABETICALLY_PROPERTY = ProtegeWriterSettings.class.getName() + ".sortAlphabetically";

    public final static String USE_XML_ENTITIES_PROPERTY = ProtegeWriterSettings.class.getName() + ".useXMLEntities";


    public ProtegeWriterSettings(OWLModel owlModel) {
        this.owlModel = owlModel;
    }


    public boolean getUseXMLEntities() {
        return !Boolean.FALSE.equals(owlModel.getOWLProject().getSettingsMap().getBoolean(USE_XML_ENTITIES_PROPERTY));
    }


    public boolean isSortAlphabetically() {
        return Boolean.TRUE.equals(owlModel.getOWLProject().getSettingsMap().getBoolean(SORT_ALPHABETICALLY_PROPERTY));
    }


    public void setSortAlphabetically(boolean value) {
        owlModel.getOWLProject().getSettingsMap().setBoolean(SORT_ALPHABETICALLY_PROPERTY, Boolean.valueOf(value));
    }


    public void setUseXMLEntities(boolean value) {
        owlModel.getOWLProject().getSettingsMap().setBoolean(USE_XML_ENTITIES_PROPERTY, Boolean.valueOf(value));
    }
}
