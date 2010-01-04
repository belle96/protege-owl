package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A base class for OWLModelActions.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLModelAction implements OWLModelAction {

    private Set listeners = new HashSet();

    private boolean suitable = true;

    public static final String CODE_MENU = "Code";

    public static final String OWL_MENU = "OWL";
    
    public static final String REASONING_MENU = "Reasoning";

    public static final String PROJECT_MENU = "Project";

    public static final String TOOLS_MENU = "Tools";

	public static final String WINDOW_MENU = "Window";


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }


    public void dispose() {
        // Do nothing by default - overload to remove listeners etc
    }


    public Class getIconResourceClass() {
        return OWLIcons.class;
    }


    public String getIconFileName() {
        return null;
    }


    public String getToolbarPath() {
        return null;
    }


    public boolean isSuitable(OWLModel owlModel) {
        return suitable;
    }


    public void notifyPropertyChangeListeners(String propertyName, Object oldValue, Object newValue) {
        Iterator it = new ArrayList(listeners).iterator();
        while(it.hasNext()) {
            PropertyChangeListener listener = (PropertyChangeListener) it.next();
            listener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
        }
    }


    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }
}
