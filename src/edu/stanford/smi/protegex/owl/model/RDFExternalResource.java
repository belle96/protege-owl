package edu.stanford.smi.protegex.owl.model;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.visitor.Visitable;

/**
 * An Instance that represents a reference to an external URI.
 * API users of this class should only use the methods provided below, as
 * all the others are not persisted in OWL files.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFExternalResource extends Instance, Visitable {

    /**
     * Gets the URI string of the resource being represented by this.
     *
     * @return the URI string or null if not specified yet
     */
    String getResourceURI();


    /**
     * Sets the URI string.
     *
     * @param value
     * @see #getResourceURI()
     */
    void setResourceURI(String value);
}
