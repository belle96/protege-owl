package edu.stanford.smi.protegex.owl.ui.results;

import edu.stanford.smi.protegex.owl.model.triplestore.Triple;

import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TripleDisplayUtil {

    /**
     * Does a simple depth-first traversal into the component tree and asks all
     * of them whether they can display a given Triple, until one of them says Yes.
     *
     * @param comp   the root component of traversal
     * @param triple the Triple to display
     * @return true  if triple could be displayed
     */
    public static TripleDisplay displayTriple(Component comp, Triple triple) {
        if (comp instanceof TripleDisplay && ((TripleDisplay) comp).displayTriple(triple)) {
            return (TripleDisplay) comp;
        }
        if (comp instanceof Container) {
            Container container = (Container) comp;
            for (int i = 0; i < container.getComponentCount(); i++) {
                Component component = container.getComponent(i);
                TripleDisplay tripleDisplay = displayTriple(component, triple);
                if (tripleDisplay != null) {
                    return tripleDisplay;
                }
            }
        }
        return null;
    }
}
