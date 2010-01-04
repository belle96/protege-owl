package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.ui.HeaderComponent;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.navigation.NavigationHistorySelectable;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface Hierarchy extends NavigationHistorySelectable {

    /**
     * Creates a clone of this, i.e. a Hierarchy with the same type and at
     * least a similar configuration.
     *
     * @return a clone of this
     */
    Hierarchy createClone();


    HeaderComponent getHeaderComponent();


    RDFSClass getSelectedClass();


    /**
     * Gets the title of this instance (e.g. "Asserted Hierarchy").
     *
     * @return the title for display purposes
     */
    String getTitle();


    /**
     * Gets the general type if this Hierarchy (e.g. "Subsumption").
     *
     * @return the type for display purposes
     */
    String getType();


    /**
     * Checks if this Hierarchy shall be by default synchronized with the asserted
     * class tree.
     *
     * @return true  if this hierarchy shall be synchronized by default
     */
    boolean isDefaultSynchronized();


    void setSelectedClass(RDFSClass cls);
}
