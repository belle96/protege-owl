package edu.stanford.smi.protegex.owl.ui.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import edu.stanford.smi.protege.plugin.PluginUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * A singleton managing the available OWLModelActions.
 * This can be used to populate menubars and toolbars for various platforms.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLModelActionManager {


    public static void addOWLModelActionsToMenubar(OWLModel owlModel, Adder adder) {
        List actions = getOWLModelActions();
        List menubarActions = new ArrayList();
        for (Iterator it = actions.iterator(); it.hasNext();) {
            OWLModelAction action = (OWLModelAction) it.next();
            String path = action.getMenubarPath();
            if (path != null) { // && action.isSuitable(owlModel)) {
                menubarActions.add(action);
            }
        }
        Collections.sort(menubarActions, new Comparator() {
            public int compare(Object o, Object o1) {
                OWLModelAction actionA = (OWLModelAction) o;
                OWLModelAction actionB = (OWLModelAction) o1;
                String a = actionA.getMenubarPath() + actionA.getName();
                String b = actionB.getMenubarPath() + actionB.getName();
                return a.compareTo(b);
            }
        });
        for (Iterator it = menubarActions.iterator(); it.hasNext();) {
            OWLModelAction action = (OWLModelAction) it.next();
            adder.addOWLModelAction(action);
        }
    }


    public static void addOWLModelActionsToToolbar(OWLModel owlModel, Adder adder) {
        List actions = getOWLModelActions();
        List toolbarActions = new ArrayList();
        for (Iterator it = actions.iterator(); it.hasNext();) {
            OWLModelAction action = (OWLModelAction) it.next();
            String path = action.getToolbarPath();
            if (path != null) { // && action.isSuitable(owlModel)) {
                toolbarActions.add(action);
            }
        }
        Collections.sort(toolbarActions, new Comparator() {
            public int compare(Object o, Object o1) {
                OWLModelAction actionA = (OWLModelAction) o;
                OWLModelAction actionB = (OWLModelAction) o1;
                String a = actionA.getToolbarPath() + actionA.getName();
                String b = actionB.getToolbarPath() + actionB.getName();
                return a.compareTo(b);
            }
        });
        for (Iterator it = toolbarActions.iterator(); it.hasNext();) {
            OWLModelAction action = (OWLModelAction) it.next();
            adder.addOWLModelAction(action);
        }
    }


    private static List getOWLModelActions() {
        List actions = new ArrayList();
        Class[] classes = getOWLModelActionClasses();
        for (int i = 0; i < classes.length; i++) {
            Class aClass = classes[i];
            OWLModelAction action = getOWLModelAction(aClass);
            actions.add(action);
        }
        return actions;
    }


    public static OWLModelAction getOWLModelAction(Class clazz) {
        try {
            return (OWLModelAction) clazz.newInstance();
        }
        catch (Exception ex) {
            System.err.println("[OWLModelActionManager] Fatal Error: Could not create OWLModelAction for " + clazz);
            Log.getLogger().log(Level.SEVERE, "Exception caught", ex);
            return null;
        }
    }


    public static Class[] getOWLModelActionClasses() {
        Collection clses = new ArrayList(PluginUtilities.getClassesWithAttribute("OWLModelAction", "True"));
        return (Class[]) clses.toArray(new Class[0]);
    }


    public static interface Adder {

        void addOWLModelAction(OWLModelAction action);
    }
}
