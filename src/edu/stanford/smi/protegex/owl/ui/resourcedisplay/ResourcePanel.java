package edu.stanford.smi.protegex.owl.ui.resourcedisplay;

import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protege.util.Selectable;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.widget.InferredModeWidget;

/**
 * An interface for user interface components that display a given RDFResource
 * as a form.  Instances of this are used as the main panel on the various tabs.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ResourcePanel extends InferredModeWidget, Selectable, Disposable {

    final static int DEFAULT_TYPE_CLASS = 0;

    final static int DEFAULT_TYPE_PROPERTY = 1;

    final static int DEFAULT_TYPE_INDIVIDUAL = 2;

    final static int DEFAULT_TYPE_ONTOLOGY = 3;


    RDFResource getResource();

    void setResource(RDFResource resource);

    void dispose();
}
