package edu.stanford.smi.protegex.owl.ui.navigation;

import edu.stanford.smi.protege.util.Selectable;
import edu.stanford.smi.protege.util.SelectionEvent;

/**
 * A SelectionEvent issued by NavigationHistorySelectables indicating that
 * the selection has only changed as a result of a programmatic call (in contrast
 * to a user action).  This is useful to distinguish "back" and "forward" clicks
 * from normal user navigation. 
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProgrammaticSelectionEvent extends SelectionEvent {

    public ProgrammaticSelectionEvent(Selectable selectable) {
        super(selectable, SelectionEvent.SELECTION_CHANGED);
    }
}
