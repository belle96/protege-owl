package edu.stanford.smi.protegex.owl.model.project;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractSettingsMap implements SettingsMap {

    public String getString(String key, String defaultValue) {
        String value = getString(key);
        if(value == null) {
            return defaultValue;
        }
        else {
            return value;
        }
    }


    public void setBoolean(String key, boolean value) {
        setBoolean(key, Boolean.valueOf(value));
    }


    public void setInteger(String key, int value) {
        setInteger(key, new Integer(value));
    }
}
