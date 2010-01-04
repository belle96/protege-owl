package edu.stanford.smi.protegex.owl.ui.components.annotations;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.project.SettingsMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 20, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultAnnotations {

    public final static String SETTINGS_MAP_KEY = DefaultAnnotations.class.getName();

    private OWLModel owlModel;


    public DefaultAnnotations(OWLModel owlModel) {
        this.owlModel = owlModel;
    }


    public void addDefaultAnnotationProperty(RDFProperty property) {
        getSettingsMap().setBoolean(property.getName(), true);
    }


    public Collection getDefaultAnnotationProperties() {
        SettingsMap map = getSettingsMap();
        Collection results = new ArrayList();
        Iterator names = map.listKeys();
        while(names.hasNext()) {
            String name = (String) names.next();
            RDFResource resource = owlModel.getRDFResource(name);
            if(resource instanceof RDFProperty) {
                results.add(resource);
            }
        }
        return results;
    }


    private SettingsMap getSettingsMap() {
        return owlModel.getOWLProject().getSettingsMap().getSettingsMap(SETTINGS_MAP_KEY);
    }


    public void removeDefaultAnnotationProperty(RDFProperty property) {
        getSettingsMap().remove(property.getName());
    }


    public void setDefaultAnnotationProperties(Collection properties) {
        owlModel.getOWLProject().getSettingsMap().remove(SETTINGS_MAP_KEY);
        SettingsMap newMap = getSettingsMap();
        for (Iterator it = properties.iterator(); it.hasNext();) {
            RDFProperty property = (RDFProperty) it.next();
            newMap.setBoolean(property.getName(), true);
        }
    }
}

