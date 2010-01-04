package edu.stanford.smi.protegex.owl.model.project;

import edu.stanford.smi.protege.model.Project;

/**
 * An OWLProject wrapping a traditional Protege Project.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLProject extends AbstractOWLProject {

    private Project project;

    private SettingsMap settingsMap;


    public DefaultOWLProject(Project project) {
        this.project = project;
        settingsMap = new DefaultSettingsMap(project.getSources());
    }


    public SettingsMap getSettingsMap() {
        return settingsMap;
    }
}
