package edu.stanford.smi.protegex.owl.ui.icons;

import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;

/**
 * The default implementation of IconFactory.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultIconFactory implements IconFactory {

    public Icon getIcon(RDFResource resource) {
        return resource.getIcon();
    }
}
