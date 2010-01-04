package edu.stanford.smi.protegex.owl.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import edu.stanford.smi.protege.resource.Text;
import edu.stanford.smi.protege.util.FileUtilities;
import edu.stanford.smi.protege.util.Log;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         08-Mar-2006
 */
public final class OWLText {
    private static String buildFile = "build.properties";
    private static String directory = "files";
    private static Properties props;

    static {
        try {
            props = new Properties();
            InputStream stream = FileUtilities.getResourceStream(OWLText.class, directory, buildFile);
            props.load(stream);
        } catch (IOException e) {
            Log.getLogger().severe(Log.toString(e));
        }
    }

    public static String getName() {
        return props.getProperty("name", "Prot\u00E9g\u00E9-OWL");
    }

    public static String getBuildInfo() {
        return Text.getBuildInfo();
    }

    public static String getBuildNumber() {
        return Text.getBuildNumber();
    }

    public static String getStatus() {
        return props.getProperty("build.status");
    }

    public static String getVersion() {
        return Text.getVersion();
    }

    /**
     * This method is deprecated. The latest compatible build version is not maintained.
     * @return 0
     */
    @Deprecated
    public static int getLatestCompatibleBuild(){
        return Integer.parseInt(props.getProperty("build.compatible.latest", "0"));
    }

    public static URL getAboutURL() {
    	URL aboutURL = OWLText.class.getResource("files/about-owl.html");
        return aboutURL;
    }
}
