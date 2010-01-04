package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.widget.URLWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;

import java.net.URI;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExternalResourceURLWidget extends URLWidget {

    public void commitChanges() {
        if (getInvalidTextDescription(getText()) == null) {
            super.commitChanges();
        }
    }


    protected String getInvalidTextDescription(String text) {
        try {
            if (text.startsWith("http://") ||
                    text.startsWith("file:") ||
                    text.startsWith("mailto:") ||
                    text.startsWith("urn:")) {
                new URI(text);
                if (isDuplicateURL(text)) {
                    return "This URL is already used elsewhere.\nPlease reuse the existing untyped resource.";
                }
                return null;
            }
        }
        catch (Exception ex) {
        }
        return "Not a valid URI, such as http://protege.stanford.edu";
    }


    // Only allow valid values to be assigned
    public Collection getValues() {
        String text = getText();
        if (getInvalidTextDescription(text) == null) {
            return super.getValues();
        }
        else {
            return getInstance().getDirectOwnSlotValues(getSlot());
        }
    }


    private boolean isDuplicateURL(String text) {
        Frame frame = getKnowledgeBase().getFrame(text);
        return frame != null && !getInstance().equals(frame);
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        if (cls.getKnowledgeBase() instanceof OWLModel) {
            return URLWidget.isSuitable(cls, slot, facet);
        }
        else {
            return false;
        }
    }
}
