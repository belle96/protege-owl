package edu.stanford.smi.protegex.owl.model.project;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLProject implements OWLProject {

    private Map sessionObjectMap = new HashMap();


    public Object getSessionObject(String key) {
        return sessionObjectMap.get(key);
    }


    public void setSessionObject(String key, Object object) {
        if (object == null) {
            sessionObjectMap.remove(key);
        }
        else {
            sessionObjectMap.put(key, object);
        }
    }
}
