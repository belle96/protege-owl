package edu.stanford.smi.protegex.owl.ui.results;

import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * An interface that is typically implemented by TabWidgets, allowing them to
 * interact with global actions such as searching.  For example, if a user
 * double clicks on the search results, a tab implementing this interface can
 * navigate to the selected resource.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface HostResourceDisplay {

    /**
     * Called by actions such as the view action in the FindUsagePanel or the
     * OWLTestResultsPanel.
     * This can be implemented for special handling of this action - otherwise the
     * system will pop up a new window showing the selected RDFResource.
     *
     * @param resource the resource to display
     * @return true if the navigation was handled, false for default behavior
     */
    boolean displayHostResource(RDFResource resource);
}
