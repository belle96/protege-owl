package edu.stanford.smi.protegex.owl.model.project;

import java.util.Iterator;

/**
 * A generic interface to store project-related settings and configurations.
 *
 * SettingsMaps can be nested recursively, i.e. a SettingsMap can contain
 * various other sub-maps (similar to an XML file).
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SettingsMap {


    Boolean getBoolean(String key);


    Integer getInteger(String key);


    /**
     * Gets or creates a nested SettingsMap.
     * @param key  the name of the nested map.
     * @return an existing or ne SettingsMap
     */
    SettingsMap getSettingsMap(String key);


    String getString(String key);


    String getString(String key, String defaultValue);


    Iterator listKeys();


    void remove(String key);


    void setBoolean(String key, boolean value);


    void setBoolean(String key, Boolean value);


    void setInteger(String key, int value);


    void setInteger(String key, Integer value);


    void setString(String key, String value);
}
