package edu.stanford.smi.protegex.owl.ui.navigation;

import edu.stanford.smi.protege.util.Selectable;
import edu.stanford.smi.protegex.owl.ui.results.HostResourceDisplay;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface NavigationHistoryTabWidget extends HostResourceDisplay {

    Selectable getNestedSelectable();
}
