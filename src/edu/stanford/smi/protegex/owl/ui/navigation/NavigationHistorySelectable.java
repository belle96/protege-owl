package edu.stanford.smi.protegex.owl.ui.navigation;

import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protege.util.Selectable;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * A visual user interface component that provides a navigation history.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface NavigationHistorySelectable extends Selectable, Disposable {

    /**
     * Performs the selection in the associated component(s).
     *
     * @param resource the resource to navigate to
     */
    void navigateToResource(RDFResource resource);
}
