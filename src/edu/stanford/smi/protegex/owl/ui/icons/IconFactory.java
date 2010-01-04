package edu.stanford.smi.protegex.owl.ui.icons;

import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;

/**
 * An interface for objects that can create an Icon for a given RDFResource.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface IconFactory {

    Icon getIcon(RDFResource resource);
}
