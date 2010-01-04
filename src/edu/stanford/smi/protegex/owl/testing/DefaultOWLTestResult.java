package edu.stanford.smi.protegex.owl.testing;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLTestResult implements OWLTestResult {

    private Icon icon;

    private String message;

    private RDFResource source;

    private OWLTest test;

    private int type;

    private Object userObject;


    public DefaultOWLTestResult(String message,
                                RDFResource source,
                                int type,
                                OWLTest test) {
        this(message, source, type, test, null);
    }


    public DefaultOWLTestResult(String message,
                                RDFResource source,
                                int type,
                                OWLTest test,
                                Icon icon) {
        this.message = message;
        this.source = source;
        this.type = type;
        this.test = test;
        this.icon = icon;
    }


    public RDFResource getHost() {
        return source;
    }


    public Icon getIcon() {
        if (icon == null) {
            int type = getType();
            if (type == OWLTestResult.TYPE_ERROR) {
                return OWLIcons.getOWLTestErrorIcon();
            }
            else if (type == OWLTestResult.TYPE_WARNING) {
                return OWLIcons.getOWLTestWarningIcon();
            }
            else if (type == OWLTestResult.TYPE_OWL_FULL) {
                return OWLIcons.getOWLFullIcon();
            }
            else {
                return Icons.getBlankIcon();
            }
        }
        else {
            return icon;
        }
    }


    public String getMessage() {
        return message;
    }


    public OWLTest getOWLTest() {
        return test;
    }


    public int getType() {
        return type;
    }


    public String getTypeString() {
        if (type == OWLTestResult.TYPE_ERROR) {
            return "Error";
        }
        else if (type == OWLTestResult.TYPE_WARNING) {
            return "Warning";
        }
        else if (type == OWLTestResult.TYPE_OWL_FULL) {
            return "OWL Full";
        }
        else {
            return "";
        }
    }


    public Object getUserObject() {
        return userObject;
    }


    public void setUserObject(Object value) {
        userObject = value;
    }


    public String toString() {
        String hostStr = getHost() == null ? "" : " (at " + getHost().getBrowserText() + ")";
        return getTypeString() + hostStr + ": " + getMessage();
    }
}
