package edu.stanford.smi.protegex.owl.ui.results;

import edu.stanford.smi.protegex.owl.model.triplestore.Triple;

/**
 * An interface for tabs that can display triples.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface TripleDisplay {

    /**
     * Attempts to display a given triple.
     * Implementing methods should try to highlight the triple on the UI
     * or report false if it cannot handle the triple.
     *
     * @param triple the Triple to show
     * @return true  if the Triple could be shown, otherwise false
     */
    boolean displayTriple(Triple triple);
}
