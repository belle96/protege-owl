package edu.stanford.smi.protegex.owl.ui.actions.triple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import edu.stanford.smi.protege.plugin.PluginUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.triplestore.Triple;

/**
 * A singleton managing the available TripleActions.
 * This can be used to populate context menus for various platforms.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TripleActionManager {

    private static Map<Class, TripleAction> map = new HashMap<Class, TripleAction> ();


    public static void addTripleActionsToMenu(Triple triple, Adder adder) {
        List<TripleAction> actions = getTripleActions();
        List<TripleAction> menubarActions = new ArrayList<TripleAction>();
        for (Iterator<TripleAction> it = actions.iterator(); it.hasNext();) {
            TripleAction action = it.next();
            if (action.isSuitable(triple)) {
                menubarActions.add(action);
            }
        }
        Collections.sort(menubarActions, new Comparator<TripleAction>() {
            public int compare(TripleAction actionA, TripleAction actionB) {
                String a = actionA.getName();
                String b = actionB.getName();
                return a.compareTo(b);
            }
        });
        for (Iterator<TripleAction> it = menubarActions.iterator(); it.hasNext();) {
            TripleAction action = it.next();
            adder.addTripleAction(action);
        }
    }


    @SuppressWarnings("unchecked")
    private static List<TripleAction> getTripleActions() {
        List<TripleAction> actions = new ArrayList<TripleAction>();
        Class[] classes = getTripleActionClasses();
        for (int i = 0; i < classes.length; i++) {
            Class aClass = classes[i];
            TripleAction action = getTripleAction(aClass);
            actions.add(action);
        }
        return actions;
    }


    public static TripleAction getTripleAction(Class clazz) {
        TripleAction action = (TripleAction) map.get(clazz);
        if (action == null) {
            try {
                action = (TripleAction) clazz.newInstance();
                map.put(clazz, action);
            }
            catch (Exception ex) {
                System.err.println("[TripleActionManager] Fatal Error: Could not create TripleAction for " + clazz);
                Log.getLogger().log(Level.SEVERE, "Exception caught", ex);
            }
        }
        return action;
    }


    @SuppressWarnings("unchecked")
    public static Class[] getTripleActionClasses() {
        Collection<Class> clses = new ArrayList<Class>(PluginUtilities.getClassesWithAttribute("TripleAction", "True"));
        return (Class[]) clses.toArray(new Class[0]);
    }


    public static interface Adder {

        void addTripleAction(TripleAction action);
    }
}
