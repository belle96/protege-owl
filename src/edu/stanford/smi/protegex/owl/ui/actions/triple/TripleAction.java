package edu.stanford.smi.protegex.owl.ui.actions.triple;

import edu.stanford.smi.protegex.owl.model.triplestore.Triple;
import edu.stanford.smi.protegex.owl.ui.actions.IconOwner;

/**
 * An object representing an "action" that can be performed on a given Triple.
 * Instances of this class can be added to context menus of single property values.
 * A typical use case for this is to add right-click menu items to the TriplesTable.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface TripleAction extends IconOwner {

    /**
     * Gets an (optional) group name, allowing tools to display action items in groups.
     *
     * @return a group name or null
     */
    String getGroup();


    /**
     * Gets the name of the action to appear on screen.
     *
     * @return the name of this action (should not be null)
     */
    String getName();


    /**
     * Tests if this action can be applied to a given Triple.
     *
     * @param triple the Triple to apply this to
     * @return true if this can be applied to triple
     */
    boolean isSuitable(Triple triple);


    /**
     * Performs the action to a given Triple.
     *
     * @param triple the Triple to operate on
     */
    void run(Triple triple);
}
