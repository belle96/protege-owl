package edu.stanford.smi.protegex.owl.model.classdisplay;

import edu.stanford.smi.protege.plugin.PluginUtilities;
import edu.stanford.smi.protege.util.ApplicationProperties;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.classdisplay.manchester.ManchesterOWLClassDisplay;

import java.util.logging.Level;

/**
 * A Singleton object that manages the available OWLClassDisplays.
 * New displays can be registered by plugins by means of a manifest entry
 * tagged with "OWLClassRenderer=true".
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLClassDisplayFactory {

    public final static String APPLICATION_PROPERTY = "OWLClassDisplayFactory" + ".default";

    private static OWLClassDisplay defaultDisplay;

    public final static String KEY = "OWLClassDisplay";


    public static Class[] getAvailableDisplayClasses() {
        return (Class[]) PluginUtilities.getClassesWithAttribute(KEY, "true").toArray(new Class[0]);
    }


    public static OWLClassDisplay getDefaultDisplay() {
        if (defaultDisplay == null) {
            String className = ApplicationProperties.getString(APPLICATION_PROPERTY);
            if (className != null) {
                try {
                    Class c = Class.forName(className);// PluginUtilities.forName(className);
                    defaultDisplay = getDisplay(c);
                }
                catch (Exception ex) {
                    Log.getLogger().log(Level.WARNING, "Could not create OWLClassDisplay of type " + className);
                    defaultDisplay = new ManchesterOWLClassDisplay();
                }
            }
            else {
                defaultDisplay = new ManchesterOWLClassDisplay();
            }
        }
        return defaultDisplay;
    }


    public static OWLClassDisplay getDisplay(Class type) {
        try {
            return (OWLClassDisplay) type.newInstance();
        }
        catch (Throwable ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }


    public static void setDefaultDisplay(OWLClassDisplay renderer) {
        defaultDisplay = renderer;
        ApplicationProperties.setString(APPLICATION_PROPERTY, renderer.getClass().getName());
    }
}
