package edu.stanford.smi.protegex.owl.ui.resourcedisplay;

import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;

/**
 * The base interface of plugins that can add components to the bottom-right
 * area of an ResourceDisplay.  These plugins are identified with
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ResourceDisplayPlugin {

    /**
     * Called whenever a new instance is displayed in a ResourceDisplay.
     * This method should check the type of the Resource, and depending on
     * this add components to the hostPanel (which is the container of
     * arbitrary components in the bottom right area of the screen.
     *
     * @param resource  the currently selected Frame
     * @param hostPanel the JPanel that can be modified
     */
    void initResourceDisplay(RDFResource resource, JPanel hostPanel);
}
