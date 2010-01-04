package edu.stanford.smi.protegex.owl.model.project;

import edu.stanford.smi.protege.util.PropertyList;

import java.util.Iterator;

/**
 * An OWLProject wrapping a traditional Protege Project.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultSettingsMap extends AbstractSettingsMap {

    private PropertyList propertyList;


    public DefaultSettingsMap(PropertyList propertyList) {
        this.propertyList = propertyList;
    }


    public Boolean getBoolean(String key) {
        return propertyList.getBoolean(key);
    }


    public Integer getInteger(String key) {
        return propertyList.getInteger(key);
    }


    public SettingsMap getSettingsMap(String key) {
        PropertyList list = propertyList.getPropertyList(key);
        return new DefaultSettingsMap(list);
    }


    public String getString(String key) {
        return propertyList.getString(key);
    }


    public Iterator listKeys() {
        return propertyList.getNames().iterator();
    }


    public void remove(String key) {
        propertyList.remove(key);
    }


    public void setBoolean(String key, Boolean value) {
        if (value == null) {
            propertyList.remove(key);
        }
        else {
            propertyList.setBoolean(key, value);
        }
    }


    public void setInteger(String key, Integer value) {
        if (value == null) {
            propertyList.remove(key);
        }
        else {
            propertyList.setInteger(key, value);
        }
    }


    public void setString(String key, String value) {
        if (value == null) {
            propertyList.remove(key);
        }
        else {
            propertyList.setString(key, value);
        }
    }
}
