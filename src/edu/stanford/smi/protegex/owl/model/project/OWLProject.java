package edu.stanford.smi.protegex.owl.model.project;

/**
 * A generic interface to store project-related settings and configurations.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLProject {

    /**
     * Gets a session object for the associated OWLModel.
     * Session objects can be associated with an OWLModel, but they are not stored
     * when the project is closed.
     * @param key  the key of the object
     * @return the object or null
     */
    Object getSessionObject(String key);


    /**
     * Gets the projects' SettingsMap.
     * @return the SettingsMap
     */
    SettingsMap getSettingsMap();


    /**
     * Sets a session object.
     * @param key  the key of the object
     * @param object  the new value or null to delete the entry
     */
    void setSessionObject(String key, Object object);
}
